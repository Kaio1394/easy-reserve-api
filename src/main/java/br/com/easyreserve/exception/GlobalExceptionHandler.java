package br.com.easyreserve.exception;

import br.com.easyreserve.common.model.ErrorResponse;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final MessageSource messageSource;

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handle(Exception ex){
        return ResponseEntity
                .badRequest()
                .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), LocalDateTime.now()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), ex.getMessage(), LocalDateTime.now()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(
            MethodArgumentNotValidException ex) {

        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));

        return ResponseEntity.badRequest()
                .body(new ErrorResponse(
                        400,
                        message,
                        LocalDateTime.now()
                ));
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<?> handleUserAlreadyExistsException(UserAlreadyExistsException ex){
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(
                        HttpStatus.CONFLICT.value(),
                        messageSource.getMessage(
                                "exception.user.already.exists",
                                new Object[]{ex.getUsername()},
                                LocaleContextHolder.getLocale()
                        ),
                        LocalDateTime.now()));
    }

    @ExceptionHandler(UserAlreadyExistWithEmailException.class)
    public ResponseEntity<?> handleUserAlreadyExistWithEmailException(UserAlreadyExistWithEmailException ex){
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(
                        HttpStatus.CONFLICT.value(),
                        messageSource.getMessage(
                                "exception.user.already.exists.with.email",
                                new Object[]{},
                                LocaleContextHolder.getLocale()
                        ),
                        LocalDateTime.now()));
    }

    @ExceptionHandler(WorkspaceAlreadyExistsException.class)
    public ResponseEntity<?> handleWorkspaceAlreadyExistsException(WorkspaceAlreadyExistsException ex){
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(
                        HttpStatus.CONFLICT.value(),
                        messageSource.getMessage(
                                "exception.workspace.already.exists",
                                new Object[]{ex.getSlug()},
                                LocaleContextHolder.getLocale()
                        ),
                        LocalDateTime.now()));
    }

    @ExceptionHandler(ModuleNotFoundException.class)
    public ResponseEntity<?> handleModuleNotFoundException(ModuleNotFoundException ex){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(
                        HttpStatus.NOT_FOUND.value(),
                        messageSource.getMessage(
                                "exception.module.not.found",
                                new Object[]{ex.getSlug()},
                                LocaleContextHolder.getLocale()
                        ),
                        LocalDateTime.now()));
    }

    @ExceptionHandler(ModuleAlreadyExistsException.class)
    public ResponseEntity<?> handleModuleAlreadyExistsException(ModuleAlreadyExistsException ex){
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(
                        HttpStatus.CONFLICT.value(),
                        messageSource.getMessage(
                                "exception.module.already.exists",
                                new Object[]{ex.getSlug()},
                                LocaleContextHolder.getLocale()
                        ),
                        LocalDateTime.now()));
    }

    @ExceptionHandler(EnvironmentAlreadyExistsException.class)
    public ResponseEntity<?> handleEnvironmentAlreadyExistsException(EnvironmentAlreadyExistsException ex){
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(
                        HttpStatus.CONFLICT.value(),
                        messageSource.getMessage(
                                "exception.environment.already.exists",
                                new Object[]{ex.getEnvironmentName(), ex.getWorkspaceName()},
                                LocaleContextHolder.getLocale()
                        ),
                        LocalDateTime.now()));
    }

}
