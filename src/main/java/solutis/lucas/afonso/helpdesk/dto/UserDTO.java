package solutis.lucas.afonso.helpdesk.dto;

import java.time.LocalDateTime;

import solutis.lucas.afonso.helpdesk.entities.User;
import solutis.lucas.afonso.helpdesk.entities.UserRole;

public record UserDTO(Long id, String name, String email, UserRole userRole, Boolean active, LocalDateTime createdAt) {
    public UserDTO(User user) {
        this(user.getId(), user.getName(), user.getEmail(), user.getUserRole(), user.getActive(), user.getCreatedAt());
    }
}
