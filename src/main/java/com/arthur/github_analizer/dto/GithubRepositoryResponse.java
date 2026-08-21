package com.arthur.github_analizer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

//os dados que recebemos do GitHub sobre cada repositório
public record GithubRepositoryResponse(
        String name,

        @JsonProperty("stargazers_count")
        long stargazersCount
) {
}