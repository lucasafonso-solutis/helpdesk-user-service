package solutis.lucas.afonso.helpdesk.services;

import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.AccessDeniedException;

import solutis.lucas.afonso.helpdesk.dto.UserDTO;
import solutis.lucas.afonso.helpdesk.dto.UserForm;
import solutis.lucas.afonso.helpdesk.entities.User;
import solutis.lucas.afonso.helpdesk.repository.UserRepository;
import solutis.lucas.afonso.helpdesk.security.JwtPrincipal;

@Service
public class UserService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEnconder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEnconder = passwordEncoder;
    }

    public UserDTO create(UserForm userForm) {
        String encryptedPassword = passwordEnconder.encode(userForm.passwordHash());
        UserForm encryptedUserForm = new UserForm(userForm.id(), userForm.name(), userForm.email(), encryptedPassword,
                                                    userForm.userRole(), userForm.active(), userForm.createdAt());
        User user = new User(encryptedUserForm);
        user = userRepository.save(user);

        return new UserDTO(user);
    }

    public List<UserDTO> findById(Long id) {
        return this.userRepository.findById(id)
            .stream()
            .filter(user -> Boolean.TRUE.equals(user.getActive()))
            .map(UserDTO::new).toList();
    }

    public List<UserDTO> list() {
        return this.userRepository.findAll().stream()
            .filter(user -> Boolean.TRUE.equals(user.getActive()))
            .map(UserDTO::new)
            .toList();
    }

    public UserDTO update(Long id, UserDTO userDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getPrincipal() == null) {
            throw new AccessDeniedException("Authentication required");
        }

        if (!(authentication.getPrincipal() instanceof JwtPrincipal principal)) {
            throw new AccessDeniedException("Invalid authenticated principal");
        }

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

        Long loggedUserId = principal.getUserId();
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!isAdmin && !loggedUserId.equals(user.getId())) {
            throw new AccessDeniedException("You don't have permission");
        }

        if (!isAdmin) {
            if (userDTO.userRole() != null && userDTO.userRole() != user.getUserRole()) {
                throw new AccessDeniedException("You can't change user role");
            }

            if (userDTO.active() != null && !userDTO.active().equals(user.getActive())) {
                throw new AccessDeniedException("You can't change active status");
            }
        }

        user.setName(userDTO.name());
        user.setEmail(userDTO.email());

        if (isAdmin) {
            if (userDTO.userRole() != null) {
                user.setUserRole(userDTO.userRole());
            }

            if (userDTO.active() != null) {
                user.setActive(userDTO.active());
            }
        }

        return new UserDTO(userRepository.save(user));
    }

    public List<UserDTO> deactivateUser(Long id) {
        return userRepository.findById(id).stream().map(user -> {
            user.setActive(false);
            return new UserDTO(userRepository.save(user));
        }).toList();
    }
}
