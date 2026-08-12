package com.arthur.github_analizer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GithubResponseApi(
        String login,
        String name,
        String company,
        String location,
        String bio,

        @JsonProperty("public_repos")
        int publicRepos,

        int followers,
        int following,

        @JsonProperty("created_at")
        String createdAt
) {
}