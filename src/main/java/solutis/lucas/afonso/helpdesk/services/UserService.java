package solutis.lucas.afonso.helpdesk.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import solutis.lucas.afonso.helpdesk.dto.UserDTO;
import solutis.lucas.afonso.helpdesk.entities.User;
import solutis.lucas.afonso.helpdesk.repository.UserRepository;

@Service
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDTO create(UserDTO userDTO) {
        User user = new User(userDTO);
        user = userRepository.save(user);

        return new UserDTO(user);
    }

    public Optional<UserDTO> findById(Long id) {
        return this.userRepository.findById(id)
            .filter(user -> Boolean.TRUE.equals(user.getActive()))
            .map(UserDTO::new);
    }

    public List<UserDTO> list() {
        return this.userRepository.findAll().stream()
            .filter(user -> Boolean.TRUE.equals(user.getActive()))
            .map(UserDTO::new)
            .toList();
    }

    public Optional<UserDTO> update(Long id, UserDTO userDTO) {
        return userRepository.findById(id).map(user -> {
            user.setName(userDTO.name());
            user.setEmail(userDTO.email());
            user.setUserRole(userDTO.userRole());
            return new UserDTO(userRepository.save(user));
        });
    }

    public Optional<UserDTO> deactivateUser(Long id) {
        return userRepository.findById(id).map(user -> {
            user.setActive(false);
            return new UserDTO(userRepository.save(user));
        });
    }
}
