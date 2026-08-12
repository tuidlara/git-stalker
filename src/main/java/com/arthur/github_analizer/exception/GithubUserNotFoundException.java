package com.arthur.github_analizer.exception;

public class GithubUserNotFoundException extends RuntimeException{
    public GithubUserNotFoundException(String message){
        super(message);
    }

}
