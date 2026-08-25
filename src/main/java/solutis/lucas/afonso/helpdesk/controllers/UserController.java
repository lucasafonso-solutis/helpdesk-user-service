package solutis.lucas.afonso.helpdesk.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import solutis.lucas.afonso.helpdesk.services.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Create User", description = "Create User")
    @ApiResponse(responseCode = "201", description = "Create User")
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO, UriComponentsBuilder uriComponentsBuilder) {
        UserDTO user = this.userService.create(userDTO);
        URI uri = uriComponentsBuilder.path("/users/{id}").buildAndExpand(user.id()).toUri();

        return ResponseEntity.created(uri).body(user);
    }

    @Operation(summary = "List User by ID", description = "List User By ID")
    @ApiResponse(responseCode = "200", description = "List User By ID")
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
    @GetMapping
    public List<UserDTO> list() {
        return this.userService.list();
    }

    @Operation(summary = "Update User", description = "Update User")
    @ApiResponse(responseCode = "200", description = "Update User")
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> update(@PathVariable Long id, @Valid @RequestBody UserDTO userDTO) {
        return userService.update(id, userDTO)
                .stream()
                .findFirst()
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Deactivate User", description = "Deactivate User")
    @ApiResponse(responseCode = "204", description = "User deactivated")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        return userService.deactivateUser(id)
                .stream()
                .findFirst()
                .map(user -> ResponseEntity.noContent().<Void>build())
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
