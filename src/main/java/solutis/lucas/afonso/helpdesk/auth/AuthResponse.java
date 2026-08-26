package solutis.lucas.afonso.helpdesk.auth;

public record AuthResponse(String token, String type, Long userId, String name, String role) {

}
