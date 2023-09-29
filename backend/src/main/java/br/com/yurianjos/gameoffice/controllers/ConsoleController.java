package br.com.yurianjos.gameoffice.controllers;

import br.com.yurianjos.gameoffice.domain.Console;
import br.com.yurianjos.gameoffice.services.ConsoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/console")
public class ConsoleController {

    @Autowired
    private ConsoleService consoleService;

    @GetMapping
    public ResponseEntity<List<Console>> getConsoles() {
        var response = this.consoleService.getConsoles();
        return ResponseEntity.ok().body(response);
    }
}
