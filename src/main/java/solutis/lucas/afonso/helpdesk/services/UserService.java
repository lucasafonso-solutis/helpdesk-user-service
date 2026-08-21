package solutis.lucas.afonso.helpdesk.services;

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

    public UserDTO createUser(UserDTO userDTO) {
        User user = new User(userDTO);
        user = userRepository.save(user);

        return new UserDTO(user);
    }
}
