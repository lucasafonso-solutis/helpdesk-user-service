package solutis.lucas.afonso.helpdesk.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import solutis.lucas.afonso.helpdesk.dto.UserDTO;
import solutis.lucas.afonso.helpdesk.dto.UserForm;
import solutis.lucas.afonso.helpdesk.dto.UserSummaryDTO;
import solutis.lucas.afonso.helpdesk.repository.UserRepository;
import solutis.lucas.afonso.helpdesk.services.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;
    private UserRepository userRepository;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @Operation(summary = "Create User", description = "Create User")
    @ApiResponse(responseCode = "201", description = "Create User")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserForm userForm, UriComponentsBuilder uriComponentsBuilder) {
        UserDTO user = this.userService.create(userForm);
        URI uri = uriComponentsBuilder.path("/users/{id}").buildAndExpand(user.id()).toUri();

        return ResponseEntity.created(uri).body(user);
    }

    @Operation(summary = "List User by ID", description = "List User By ID")
    @ApiResponse(responseCode = "200", description = "List User By ID")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> findById(@PathVariable Long id) {
        return userService.findById(id)
                .stream()
                .findFirst()
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "List All Users")
    @ApiResponse(responseCode = "200", description = "List All Users")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<UserDTO> list() {
        return this.userService.list();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/summary")
    public ResponseEntity<UserSummaryDTO> summary(@PathVariable Long id) {
        return userRepository.findById(id)
                .filter(user -> Boolean.TRUE.equals(user.getActive()))
                .map(user -> ResponseEntity.ok(new UserSummaryDTO(user)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Update User", description = "Update User")
    @ApiResponse(responseCode = "200", description = "Update User")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.userId")
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> update(@PathVariable Long id, @Valid @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.update(id, userDTO);
        
        return ResponseEntity.ok(updatedUser);
    }

    @Operation(summary = "Deactivate User", description = "Deactivate User")
    @ApiResponse(responseCode = "204", description = "User deactivated")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        return userService.deactivateUser(id)
                .stream()
                .findFirst()
                .map(user -> ResponseEntity.noContent().<Void>build())
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
