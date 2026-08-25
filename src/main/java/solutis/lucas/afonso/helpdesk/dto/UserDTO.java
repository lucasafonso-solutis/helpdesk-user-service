package solutis.lucas.afonso.helpdesk.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import solutis.lucas.afonso.helpdesk.entities.User;
import solutis.lucas.afonso.helpdesk.entities.UserRole;

public record UserDTO(Long id, 
                    @NotBlank(message = "Name is mandatory")
                    String name,
                    @NotBlank(message = "Email is mandatory")
                    @Email 
                    String email,
                    @NotNull(message = "Role is mandatory")
                    UserRole userRole,
                    Boolean active, LocalDateTime createdAt) {
    public UserDTO(User user) {
        this(user.getId(), user.getName(), user.getEmail(), user.getUserRole(), user.getActive(), user.getCreatedAt());
    }
}
