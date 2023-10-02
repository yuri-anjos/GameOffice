package br.com.yurianjos.gameoffice.services;

import br.com.yurianjos.gameoffice.domain.Console;
import br.com.yurianjos.gameoffice.repositories.ConsoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ConsoleService {

    private final ConsoleRepository consoleRepository;

    public List<Console> getConsoles() {
        return consoleRepository.findAll();
    }
}
