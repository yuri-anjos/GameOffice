package br.com.yurianjos.gameoffice.services;

import br.com.yurianjos.gameoffice.domain.User;
import br.com.yurianjos.gameoffice.dtos.UpdateUserRequestDTO;
import br.com.yurianjos.gameoffice.dtos.UserResponseDTO;
import br.com.yurianjos.gameoffice.dtos.exceptions.CustomException;
import br.com.yurianjos.gameoffice.repositories.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WishService wishService;

    public UserResponseDTO getUser() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var gamesWished = wishService.getWishedGamesIds(user.getId());
        return new UserResponseDTO(user, gamesWished);
    }

    public void updateUser(UpdateUserRequestDTO dto) throws CustomException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!user.getUsername().equals(dto.username()) && this.userRepository.existsByUsername(dto.username())) {
            throw new CustomException("Nome de usuário já está em uso!", HttpStatus.BAD_REQUEST.value());
        }

        if (!user.getEmail().equals(dto.email()) && this.userRepository.existsByEmail(dto.email())) {
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

        this.userRepository.save(user);
    }

    public User findById(Long id) throws CustomException {
        return this.userRepository.findById(id).orElseThrow(() -> new CustomException("Usuário não encontrado!", HttpStatus.NOT_FOUND.value()));
    }
}
