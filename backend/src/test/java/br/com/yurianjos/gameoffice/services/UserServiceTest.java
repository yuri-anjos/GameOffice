package br.com.yurianjos.gameoffice.services;

import br.com.yurianjos.gameoffice.domain.User;
import br.com.yurianjos.gameoffice.dtos.UpdateUserRequestDTO;
import br.com.yurianjos.gameoffice.dtos.UserResponseDTO;
import br.com.yurianjos.gameoffice.dtos.exceptions.CustomException;
import br.com.yurianjos.gameoffice.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private UserService underTest;

    @Mock
    private UserRepository userRepository;

    @Mock
    private WishService wishService;

    @Mock
    private ContextService contextService;

    @BeforeEach
    void setUp() {
        underTest = new UserService(userRepository, wishService, contextService);
    }

    @Test
    void getUser() {
        //given
        var user = User.builder()
                .id(1L)
                .email("email@email.com")
                .role("USER")
                .username("username")
                .build();

        var whishedGames = Arrays.asList(1L, 2L, 3L);

        var expected = new UserResponseDTO(user, whishedGames);

        //when
        when(contextService.getContextUser()).thenReturn(user);
        when(wishService.getWishedGamesIds(user.getId())).thenReturn(whishedGames);
        var response = underTest.getUser();

        //then
        verify(contextService).getContextUser();
        verify(wishService).getWishedGamesIds(any());
        assertThat(response).isEqualTo(expected);

    }

    @Test
    void updateUserThrowUsernameIsAlreadyTaken() {
        //given
        var userId = 1L;
        var oldUsername = "oldUsername";
        var oldEmail = "oldEmail@email.com";
        var password = "123";
        var confirmPassword = "123";

        var newUsername = "newUsername";
        var newEmail = "newEmail@email.com";

        var user = User.builder()
                .id(userId)
                .username(oldUsername)
                .email(oldEmail)
                .build();

        var dto = UpdateUserRequestDTO.builder()
                .username(newUsername)
                .email(newEmail)
                .password(password)
                .confirmPassword(confirmPassword)
                .build();

        //when
        when(contextService.getContextUser()).thenReturn(user);
        when(userRepository.existsByUsername(dto.username())).thenReturn(Boolean.TRUE);

        //then
        assertThatThrownBy(() -> underTest.updateUser(dto))
                .isInstanceOf(CustomException.class)
                .hasMessage("Nome de usuário já está em uso!");
        verify(contextService).getContextUser();
        verify(userRepository).existsByUsername(any());
        verify(userRepository, never()).existsByEmail(any());
    }

    @Test
    void updateUserThrowEmailIsAlreadyTaken() {
        //given
        var userId = 1L;
        var oldUsername = "oldUsername";
        var oldEmail = "oldEmail@email.com";
        var password = "123";
        var confirmPassword = "123";

        var newUsername = "newUsername";
        var newEmail = "newEmail@email.com";

        var user = User.builder()
                .id(userId)
                .username(oldUsername)
                .email(oldEmail)
                .build();

        var dto = UpdateUserRequestDTO.builder()
                .username(newUsername)
                .email(newEmail)
                .password(password)
                .confirmPassword(confirmPassword)
                .build();

        //when
        when(contextService.getContextUser()).thenReturn(user);
        when(userRepository.existsByUsername(dto.username())).thenReturn(Boolean.FALSE);
        when(userRepository.existsByEmail(dto.email())).thenReturn(Boolean.TRUE);

        //then
        assertThatThrownBy(() -> underTest.updateUser(dto))
                .isInstanceOf(CustomException.class)
                .hasMessage("Email de usuário já está em uso!");
        verify(contextService).getContextUser();
        verify(userRepository).existsByUsername(any());
        verify(userRepository).existsByEmail(any());
        verify(userRepository, never()).save(user);
    }

    @Test
    void updateUserThrowIncorrectPasswordConfirmation() {
        //given
        var userId = 1L;
        var oldUsername = "oldUsername";
        var oldEmail = "oldEmail@email.com";
        var password = "123";
        var confirmPassword = "1234";

        var newUsername = "newUsername";
        var newEmail = "newEmail@email.com";

        var user = User.builder()
                .id(userId)
                .username(oldUsername)
                .email(oldEmail)
                .build();

        var dto = UpdateUserRequestDTO.builder()
                .username(newUsername)
                .email(newEmail)
                .password(password)
                .confirmPassword(confirmPassword)
                .build();

        //when
        when(contextService.getContextUser()).thenReturn(user);
        when(userRepository.existsByUsername(dto.username())).thenReturn(Boolean.FALSE);
        when(userRepository.existsByEmail(dto.email())).thenReturn(Boolean.FALSE);

        //then
        assertThatThrownBy(() -> underTest.updateUser(dto))
                .isInstanceOf(CustomException.class)
                .hasMessage("Senha e confirmação de senha devem coincidir!");
        verify(contextService).getContextUser();
        verify(userRepository).existsByUsername(any());
        verify(userRepository).existsByEmail(any());
        verify(userRepository, never()).save(user);
    }

    @Test
    void updateUserSeccess() throws CustomException {
        //given
        var userId = 1L;
        var oldUsername = "oldUsername";
        var oldEmail = "oldEmail@email.com";
        var password = "123";
        var confirmPassword = "123";

        var newUsername = "oldUsername";
        var newEmail = "oldEmail@email.com";

        var user = User.builder()
                .id(userId)
                .username(oldUsername)
                .email(oldEmail)
                .build();

        var dto = UpdateUserRequestDTO.builder()
                .username(newUsername)
                .email(newEmail)
                .password(password)
                .confirmPassword(confirmPassword)
                .build();

        //when
        when(contextService.getContextUser()).thenReturn(user);
        underTest.updateUser(dto);

        //then
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(contextService).getContextUser();
        verify(userRepository, never()).existsByUsername(any());
        verify(userRepository, never()).existsByEmail(any());
        verify(userRepository).save(userArgumentCaptor.capture());

        var capturedUser = userArgumentCaptor.getValue();
        assertThat(capturedUser.getUsername()).isEqualTo(dto.username());
        assertThat(capturedUser.getEmail()).isEqualTo(dto.email());
        assertTrue(new BCryptPasswordEncoder().matches(dto.password(), capturedUser.getPassword()));
    }

    @Test
    void findByIdSuccess() throws CustomException {
        //given
        var userId = 1L;
        var user = User.builder().id(userId).build();

        //when
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        var response = underTest.findById(userId);

        //then
        verify(userRepository).findById(userId);
        assertThat(response).isEqualTo(user);
    }

    @Test
    void findByIdThrowError() {
        //given
        var userId = 1L;

        //when
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> underTest.findById(userId))
                .isInstanceOf(CustomException.class)
                .hasMessage("Usuário não encontrado!");
        verify(userRepository).findById(userId);
    }
}