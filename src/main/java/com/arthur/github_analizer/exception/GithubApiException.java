package com.arthur.github_analizer.exception;

public class GithubApiException extends RuntimeException{
    public GithubApiException(String message) {
        super(message);
    }
}
