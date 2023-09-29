package br.com.yurianjos.gameoffice.controllers;


import br.com.yurianjos.gameoffice.dtos.UpdateUserRequestDTO;
import br.com.yurianjos.gameoffice.dtos.UserResponseDTO;
import br.com.yurianjos.gameoffice.dtos.exceptions.CustomException;
import br.com.yurianjos.gameoffice.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping()
    public ResponseEntity<UserResponseDTO> getUser() {
        var result = this.userService.getUser();
        return ResponseEntity.ok().body(result);
    }

    @PutMapping()
    public ResponseEntity<Void> updateUser(@RequestBody @Valid UpdateUserRequestDTO dto) throws CustomException {
        this.userService.updateUser(dto);
        return ResponseEntity.ok().build();
    }
}
