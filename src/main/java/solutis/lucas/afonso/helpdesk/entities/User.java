package solutis.lucas.afonso.helpdesk.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import solutis.lucas.afonso.helpdesk.dto.UserDTO;
import solutis.lucas.afonso.helpdesk.dto.UserForm;

@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(unique = true)
    private String email;
    private String passwordHash;
    @Enumerated(EnumType.STRING)
    private UserRole UserRole;
    private Boolean active;
    private LocalDateTime createdAt;

    public User() {
    }

    public User(Long id, String name, String email, String passwordHash, UserRole userRole, Boolean active, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.UserRole = userRole;
        this.active = true;
        this.createdAt = LocalDateTime.now();
    }

    public User(UserDTO userDTO) {
        this.name = userDTO.name();
        this.email = userDTO.email();
        this.UserRole = userDTO.userRole();
        this.active = true;
        this.createdAt = LocalDateTime.now();
    }

    public User(UserForm userForm) {
        this.name = userForm.name();
        this.email = userForm.email();
        this.passwordHash = userForm.passwordHash();
        this.UserRole = userForm.userRole();
        this.active = true;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public UserRole getUserRole() {
        return UserRole;
    }

    public void setUserRole(UserRole userRole) {
        UserRole = userRole;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
