package br.com.yurianjos.gameoffice.controllers;

import br.com.yurianjos.gameoffice.domain.Roles;
import br.com.yurianjos.gameoffice.domain.User;
import br.com.yurianjos.gameoffice.dtos.LoginRequestDTO;
import br.com.yurianjos.gameoffice.dtos.LoginResponseDTO;
import br.com.yurianjos.gameoffice.dtos.RegisterRequestDTO;
import br.com.yurianjos.gameoffice.dtos.exceptions.CustomException;
import br.com.yurianjos.gameoffice.services.TokenService;
import br.com.yurianjos.gameoffice.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO dto) throws CustomException {
        var login = new UsernamePasswordAuthenticationToken(dto.email(), dto.password());

        Authentication auth;
        try {
            auth = authenticationManager.authenticate(login);
        } catch (BadCredentialsException e) {
            throw new CustomException("Credenciais Inválidas!", HttpStatus.BAD_REQUEST.value());
        }

        var response = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/register")
    public ResponseEntity<LoginResponseDTO> register(@RequestBody @Valid RegisterRequestDTO dto) throws CustomException {
        if (userRepository.existsByEmail(dto.email())) {
            throw new CustomException("Email de usuário já está em uso!", HttpStatus.BAD_REQUEST.value());
        }

        if (!dto.password().equals(dto.confirmPassword())) {
            throw new CustomException("Senha e confirmação de senha devem coincidir!", HttpStatus.BAD_REQUEST.value());
        }

        var encryptedPassword = new BCryptPasswordEncoder().encode(dto.password());
        var userData = User.builder()
                .email(dto.email())
                .name(dto.name())
                .password(encryptedPassword)
                .role(Roles.USER.name())
                .build();

        var user = userRepository.save(userData);
        var response = tokenService.generateToken(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
