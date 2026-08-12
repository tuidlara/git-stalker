package com.arthur.github_analizer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String githubNotFound(GithubUserNotFoundException e) {
        return e.getMessage();
    }

    @ExceptionHandler(GithubApiException.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public String githubApi(GithubApiException ex) {
        return ex.getMessage();
    }
}
