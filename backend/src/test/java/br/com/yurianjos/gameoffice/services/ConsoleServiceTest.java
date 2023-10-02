package br.com.yurianjos.gameoffice.services;

import br.com.yurianjos.gameoffice.domain.Console;
import br.com.yurianjos.gameoffice.repositories.ConsoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConsoleServiceTest {

    private ConsoleService underTest;

    @Mock
    private ConsoleRepository consoleRepository;

    @BeforeEach
    void setUp() {
        underTest = new ConsoleService(consoleRepository);
    }

    @Test
    void getConsoles() {
        //given
        var expected = Collections.singletonList(new Console(1L, "console"));
        
        //when
        when(consoleRepository.findAll()).thenReturn(expected);
        var result = underTest.getConsoles();

        //then
        verify(consoleRepository).findAll();
        assertThat(result).isEqualTo(expected);
    }
}