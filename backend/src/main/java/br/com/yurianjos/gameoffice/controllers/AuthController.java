package br.com.yurianjos.gameoffice.controllers;

import br.com.yurianjos.gameoffice.domain.User;
import br.com.yurianjos.gameoffice.dtos.LoginRequestDTO;
import br.com.yurianjos.gameoffice.dtos.RegisterRequestDTO;
import br.com.yurianjos.gameoffice.dtos.exceptions.CustomException;
import br.com.yurianjos.gameoffice.infra.security.TokenService;
import br.com.yurianjos.gameoffice.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/checkToken")
    public ResponseEntity checkToken() {
        return ResponseEntity.ok().body(Boolean.TRUE);
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid LoginRequestDTO dto) throws CustomException {
        var login = new UsernamePasswordAuthenticationToken(dto.username(), dto.password());

        Authentication auth;
        try {
            auth = this.authenticationManager.authenticate(login);
        } catch (BadCredentialsException e) {
            throw new CustomException("Credenciais Inválidas!", HttpStatus.BAD_REQUEST.value());
        }

        var response = this.tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterRequestDTO dto) throws CustomException {
        if (this.userRepository.existsByUsername(dto.username())) {
            throw new CustomException("Nome de usuário já está em uso!", HttpStatus.BAD_REQUEST.value());
        }

        if (this.userRepository.existsByEmail(dto.email())) {
            throw new CustomException("Email de usuário já está em uso!", HttpStatus.BAD_REQUEST.value());
        }

        if(!dto.password().equals(dto.confirmPassword())){
            throw new CustomException("Senha e confirmação de senha devem coincidir!", HttpStatus.BAD_REQUEST.value());
        }

        var encryptedPassword = new BCryptPasswordEncoder().encode(dto.password());
        var userData = new User(dto.email(), dto.username(), encryptedPassword);

        var user = this.userRepository.save(userData);
        var response = this.tokenService.generateToken(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
