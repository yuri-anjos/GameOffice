package br.com.yurianjos.gameoffice.services;

import br.com.yurianjos.gameoffice.domain.User;
import br.com.yurianjos.gameoffice.dtos.LoginResponseDTO;
import br.com.yurianjos.gameoffice.dtos.exceptions.CustomException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    private String issuer = "game-office-api";

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenService.class);

    public LoginResponseDTO generateToken(User user) throws CustomException {
        try {
            var algorithm = Algorithm.HMAC256(secret);
            var expiresAt = getExpirationDate();
            var token = JWT.create()
                    .withIssuer(issuer)
                    .withSubject(user.getUsername())
                    .withExpiresAt(expiresAt.toInstant(ZoneOffset.of("-03:00")))
                    .sign(algorithm);

            return new LoginResponseDTO(
                    token,
                    expiresAt.toInstant(ZoneOffset.of("-03:00")),
                    expiresAt
            );
        } catch (JWTCreationException exception) {
            LOGGER.error("generateToken(): ", exception);
            throw new CustomException("Erro ao gerar token!", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    public String validateToken(String token) {
        try {
            var algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer(issuer)
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            return null;
        }
    }

    private LocalDateTime getExpirationDate() {
        return LocalDateTime.now().plusHours(1);
    }
}
