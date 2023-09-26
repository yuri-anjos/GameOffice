package br.com.yurianjos.gameoffice.services;

import br.com.yurianjos.gameoffice.domain.User;
import br.com.yurianjos.gameoffice.dtos.exceptions.CustomException;
import br.com.yurianjos.gameoffice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User findById(Long id, boolean throwError) throws CustomException {
        var user = this.userRepository.findById(id).orElse(null);
        if (throwError && user == null) {
            throw new CustomException("Usuário não encontrado!", HttpStatus.NOT_FOUND.value());
        } else {
            return user;
        }
    }
}
