package com.arthur.github_analizer.dto;

public record GithubRepositoryStats(
        long totalStars,
        String mostPopularRepository
) {
}