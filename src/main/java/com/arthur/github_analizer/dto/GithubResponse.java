package com.arthur.github_analizer.dto;

public record GithubResponse(
        String login,
        String name,
        String company,
        String location,
        String bio,
        int publicRepos,
        int followers,
        int following,
        String createdAt
) {
}
