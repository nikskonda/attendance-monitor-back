package com.bntu.master.attendance.monitor.impl.config;

import com.bntu.master.attendance.monitor.api.exception.ApiError;
import com.bntu.master.attendance.monitor.api.exception.UsedInSystemException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

@RestControllerAdvice
public class ExceptionHandlerClass {

    @ExceptionHandler({UsedInSystemException.class,})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleServiceException(Exception ex, Locale locale) {
//        LOGGER.error(messageSource.getMessage(ex.getMessage(), null, Locale.getDefault()), ex);
        return new ApiError("key123", "Сущность используется в системе");
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleInternalError(Throwable t, Locale locale) {
//        LOGGER.error(t.getMessage(), t);
//        String message = messageSource.getMessage(DURING_REQUEST_PROCESSING, null, locale);
        return new ApiError("key123", t.getMessage());
    }

}
