package br.com.yurianjos.gameoffice.infra;


import br.com.yurianjos.gameoffice.dtos.exceptions.CustomException;
import br.com.yurianjos.gameoffice.dtos.ExceptionDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionDTO> handleInvalidArgument(MethodArgumentNotValidException exception) {
        var error = exception.getBindingResult().getFieldErrors().get(0);
        var message = error.getField() + ": " + error.getDefaultMessage();
        return ResponseEntity.badRequest().body(new ExceptionDTO(message));
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ExceptionDTO> customException(CustomException exception) {
        return ResponseEntity.status(exception.getStatusCode()).body(new ExceptionDTO(exception.getMessage()));
    }
}
