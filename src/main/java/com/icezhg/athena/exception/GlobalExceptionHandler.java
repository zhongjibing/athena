package com.icezhg.athena.exception;

import com.icezhg.commons.entity.RespResult;
import com.icezhg.commons.exception.ErrorCodeException;
import jakarta.servlet.ServletException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


/**
 * Created by zhongjibing on 2022/08/13.
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({AuthenticationException.class, AccessDeniedException.class})
    public ResponseEntity<Object> handleBadAuthRequest(Exception ex) throws Exception {
        log.warn("handle error: {}", ex.getMessage());
        throw ex;
    }

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public ResponseEntity<Object> handleBadRequest(HttpRequestMethodNotSupportedException ex) throws Exception {
        log.error("handle error: {}", ex.getMessage());
        return new ResponseEntity<>(RespResult.error("Method Not Allowed"), HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<Object> handleBadRequest(MethodArgumentNotValidException ex) throws Exception {
        log.error("handle error: {}", ex.getMessage());
        return new ResponseEntity<>(RespResult.error("Bad Parameters"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ServletException.class, TypeMismatchException.class, HttpMessageNotReadableException.class,
            BindException.class})
    public ResponseEntity<Object> handleBadRequest(Exception ex) throws Exception {
        log.error("handle error: {}", ex.getMessage());
        return new ResponseEntity<>(RespResult.error(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ConversionNotSupportedException.class, HttpMessageConversionException.class,
            HttpMessageNotWritableException.class})
    public ResponseEntity<Object> handleInternalException(Exception ex) throws Exception {
        log.error("handle error: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(RespResult.error("System Busy"), HttpStatus.OK);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Object> handleDataAccessException(Exception ex) throws Exception {
        log.warn("handle error: {}", ex.getCause().getMessage());
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ErrorCodeException.class)
    public ResponseEntity<Object> handleErrorCodeException(Exception ex) throws Exception {
        log.warn("handle error: {}", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception ex) throws Exception {
        log.warn("handle error: {}-{}", ex.getClass().getName(), ex.getMessage(), ex);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
