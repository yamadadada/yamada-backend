package com.yamada.handler;

import com.yamada.vo.ReturnVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionErrorHandler {

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<ReturnVO> securityHandler(SecurityException e) {
        log.warn("发生SecurityException异常", e);
        return new ResponseEntity<>(
                ReturnVO.builder()
                        .code(HttpStatus.UNAUTHORIZED.value())
                        .msg(e.getMessage())
                        .build(),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ReturnVO> validHandler(MethodArgumentNotValidException e) {
        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
        StringBuilder sb = new StringBuilder();
        allErrors.forEach(objectError -> sb.append(objectError.getDefaultMessage()).append(";"));
        return new ResponseEntity<>(
                ReturnVO.builder()
                        .code(HttpStatus.BAD_REQUEST.value())
                        .msg(sb.toString())
                        .build(),
                HttpStatus.BAD_REQUEST
        );
    }
}
