package com.arthur.github_analizer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GithubRepositoryResponse(
        String name,

        @JsonProperty("stargazers_count")
        long stargazersCount
) {
}