package com.arthur.github_analizer.dto;

import java.util.List;

//guardar os resultados calculados
public record GithubRepositoryStats(
        long totalStars,
        String mostPopularRepository,
        List<String> topLanguages
) {
}