package ru.itmo.clouds.controller.misc;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import javax.persistence.EntityNotFoundException;

@Data
@AllArgsConstructor
 class MessageResponse{
     String message;
}
@ControllerAdvice
class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = EntityNotFoundException.class)
    protected ResponseEntity<Object>  handleNotFound(Exception ex, WebRequest request) {
        return handleExceptionInternal(ex,new  MessageResponse(ex.getMessage()),
           new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }


    @ExceptionHandler(value = IllegalStateException.class)
    protected ResponseEntity<Object>  handleAlreadyRegistered(Exception ex, WebRequest request) {
        return handleExceptionInternal(ex, new MessageResponse(ex.getMessage()),
           new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = IllegalAccessException.class)
    protected  ResponseEntity<Object> handleForbidden(Exception ex, WebRequest request) {
        return handleExceptionInternal(ex, "Access denied!",
           new HttpHeaders(), HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Object>  handleCommonException(Exception ex, WebRequest request) {
        return handleExceptionInternal(ex,new  MessageResponse(ex.getMessage()),
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

}