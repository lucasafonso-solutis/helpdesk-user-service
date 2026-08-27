package solutis.lucas.afonso.helpdesk.dto;

import solutis.lucas.afonso.helpdesk.entities.User;
import solutis.lucas.afonso.helpdesk.entities.UserRole;

public record UserSummaryDTO(Long id, String name, UserRole userRole) {
    public UserSummaryDTO(User user) {
        this(user.getId(), user.getName(), user.getUserRole());
    }
}