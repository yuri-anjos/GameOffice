package br.com.yurianjos.gameoffice.services;

import br.com.yurianjos.gameoffice.domain.Console;
import br.com.yurianjos.gameoffice.repositories.ConsoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsoleService {

    @Autowired
    private ConsoleRepository consoleRepository;

    public List<Console> getConsoles() {
        return this.consoleRepository.findAll();
    }
}
