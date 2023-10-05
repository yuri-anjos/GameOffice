package br.com.yurianjos.gameoffice.services;

import br.com.yurianjos.gameoffice.domain.User;
import br.com.yurianjos.gameoffice.dtos.UpdateUserRequestDTO;
import br.com.yurianjos.gameoffice.dtos.UserResponseDTO;
import br.com.yurianjos.gameoffice.dtos.exceptions.CustomException;
import br.com.yurianjos.gameoffice.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    private final WishService wishService;

    private final ContextService contextService;

    public UserResponseDTO getUser() {
        var user = contextService.getContextUser();
        var whishedGames = wishService.getWishedGamesIds(user.getId());
        return new UserResponseDTO(user, whishedGames);
    }

    public void updateUser(UpdateUserRequestDTO dto) throws CustomException {
        var user = contextService.getContextUser();

        if (!user.getUsername().equals(dto.username()) && Boolean.TRUE.equals(userRepository.existsByUsername(dto.username()))) {
            throw new CustomException("Nome de usuário já está em uso!", HttpStatus.BAD_REQUEST.value());
        }

        if (!user.getEmail().equals(dto.email()) && Boolean.TRUE.equals(userRepository.existsByEmail(dto.email()))) {
            throw new CustomException("Email de usuário já está em uso!", HttpStatus.BAD_REQUEST.value());
        }

        if (StringUtils.isNotBlank(dto.password())) {
            if (!dto.password().equals(dto.confirmPassword())) {
                throw new CustomException("Senha e confirmação de senha devem coincidir!", HttpStatus.BAD_REQUEST.value());
            }

            var encryptedPassword = new BCryptPasswordEncoder().encode(dto.password());
            user.setPassword(encryptedPassword);
        }

        user.setEmail(dto.email());
        user.setUsername(dto.username());

        userRepository.save(user);
    }

    public User findById(Long id) throws CustomException {
        return userRepository.findById(id).orElseThrow(
                () -> new CustomException("Usuário não encontrado!", HttpStatus.NOT_FOUND.value()));
    }
}
