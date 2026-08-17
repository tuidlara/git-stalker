package com.arthur.github_analizer.dto;

import java.util.List;

public record GithubRepositoryStats(
        long totalStars,
        String mostPopularRepository,
        List<String> topLanguages
) {
}