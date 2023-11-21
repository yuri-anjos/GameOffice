package br.com.yurianjos.gameoffice.services;

import br.com.yurianjos.gameoffice.domain.User;
import br.com.yurianjos.gameoffice.dtos.ComboDTO;
import br.com.yurianjos.gameoffice.dtos.UpdateUserRequestDTO;
import br.com.yurianjos.gameoffice.dtos.UserResponseDTO;
import br.com.yurianjos.gameoffice.dtos.exceptions.CustomException;
import br.com.yurianjos.gameoffice.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

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
        user.setName(dto.name());

        userRepository.save(user);
    }

    public List<ComboDTO> searchUsers(int page, int size, String search) {
        PageRequest pageRequest = PageRequest.of(
                page,
                size,
                Sort.Direction.ASC,
                "name");

        var result = userRepository.searchNonAdminUsers(search, pageRequest);

        return result.stream().map(
                        user -> new ComboDTO(user.getId(), user.getName()))
                .toList();
    }

    public UserResponseDTO findUser(Long userID) throws CustomException {
        var result = this.findById(userID);
        return new UserResponseDTO(result, null);
    }

    public User findById(Long id) throws CustomException {
        return userRepository.findById(id)
                .orElseThrow(() -> new CustomException("Usuário não encontrado!", HttpStatus.NOT_FOUND.value()));
    }
}
