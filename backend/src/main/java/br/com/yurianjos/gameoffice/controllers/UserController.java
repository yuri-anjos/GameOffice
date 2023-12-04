package br.com.yurianjos.gameoffice.controllers;


import br.com.yurianjos.gameoffice.dtos.ComboDTO;
import br.com.yurianjos.gameoffice.dtos.UpdateUserRequestDTO;
import br.com.yurianjos.gameoffice.dtos.UserResponseDTO;
import br.com.yurianjos.gameoffice.dtos.exceptions.CustomException;
import br.com.yurianjos.gameoffice.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping()
    public ResponseEntity<UserResponseDTO> getUser() {
        var result = userService.getUser();
        return ResponseEntity.ok().body(result);
    }

    @PutMapping()
    public ResponseEntity<Void> updateUser(@RequestBody @Valid UpdateUserRequestDTO dto) throws CustomException {
        userService.updateUser(dto);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/combos")
    public ResponseEntity<List<ComboDTO>> searchUsers(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "5") int size,
            @RequestParam(required = false) String search) {
        var result = userService.searchUsers(page, size, search);
        return ResponseEntity.ok().body(result);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{userID}")
    public ResponseEntity<UserResponseDTO> findUser(@PathVariable Long userID) throws CustomException {
        var result = userService.findUser(userID);
        return ResponseEntity.ok().body(result);
    }
}
