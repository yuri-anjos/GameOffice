package br.com.yurianjos.gameoffice.services;

import br.com.yurianjos.gameoffice.domain.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ContextService {

    public User getContextUser(){
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
