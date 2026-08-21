package solutis.lucas.afonso.helpdesk.controllers;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    @ApiResponse(responseCode = "200", description = "Create User")
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO, UriComponentsBuilder uriComponentsBuilder) {
        UserDTO user = this.userService.createUser(userDTO);
        URI uri = uriComponentsBuilder.path("/users/{id}").buildAndExpand(user.id()).toUri();

        return ResponseEntity.created(uri).body(user);
    }
}
