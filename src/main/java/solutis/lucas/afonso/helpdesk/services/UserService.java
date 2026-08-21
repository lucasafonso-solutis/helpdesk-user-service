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
        return this.userRepository.findById(id).map(UserDTO::new);
    }

    public List<UserDTO> list() {
        return this.userRepository.findAll().stream().map(UserDTO::new).toList();
    }
}
