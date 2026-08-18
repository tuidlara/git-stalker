package com.arthur.github_analizer.service;

import com.arthur.github_analizer.dto.GithubRepositoryResponse;
import com.arthur.github_analizer.dto.GithubRepositoryStats;
import com.arthur.github_analizer.dto.GithubResponse;
import com.arthur.github_analizer.dto.GithubResponseApi;
import com.arthur.github_analizer.exception.GithubApiException;
import com.arthur.github_analizer.exception.GithubUserNotFoundException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class GithubService {

    private final String githubToken;
    private final RestClient restClient;

    public GithubService(RestClient restClient) {
        this.githubToken = System.getenv("GITHUB_TOKEN");
        this.restClient = restClient;
    }

    public GithubResponse getGithubProfile(String name) {

        String url = "https://api.github.com/users/" + name;

        try {

            GithubResponseApi response = restClient.get()
                    .uri(url)
                    .header("Authorization", "Bearer " + githubToken)
                    .retrieve()
                    .body(GithubResponseApi.class);

            if (response == null) {
                throw new GithubApiException("Github API returned an empty response");
            }

            GithubRepositoryStats stats = getRepositoryStats(name);

            return new GithubResponse(
                    response.login(),
                    response.name(),
                    response.company(),
                    response.location(),
                    response.bio(),
                    response.publicRepos(),
                    response.followers(),
                    response.following(),
                    response.createdAt(),
                    stats.totalStars(),
                    stats.mostPopularRepository(),
                    stats.topLanguages()

            );
        } catch (HttpClientErrorException.NotFound e) {
            throw new GithubUserNotFoundException("Github not found");
        } catch (HttpServerErrorException e) {
            throw new GithubApiException("Github API is currently unavailable");
        }

    }

    private GithubRepositoryStats getRepositoryStats(String name) {

        long totalStars = 0;
        long maiorNumeroDeEstrelas = 0;
        String repositorioMaisFamoso = null;

        Map<String, Long> languageTotals = new HashMap<>();

        int page = 1;

        while (true) {

            String url = "https://api.github.com/users/" + name
                    + "/repos?per_page=100&page=" + page;

            GithubRepositoryResponse[] repos = restClient.get()
                    .uri(url)
                    .header("Authorization", "Bearer " + githubToken)
                    .retrieve()
                    .body(GithubRepositoryResponse[].class);

            if (repos == null || repos.length == 0) {
                break;
            }

            List<CompletableFuture<Map<String, Long>>> futures = new ArrayList<>();

            for (GithubRepositoryResponse repo : repos) {

                CompletableFuture<Map<String, Long>> future =
                        getRepositoryLanguages(name, repo.name());
                futures.add(future);

                totalStars += repo.stargazersCount();

                if (repo.stargazersCount() > maiorNumeroDeEstrelas) {
                    maiorNumeroDeEstrelas = repo.stargazersCount();
                    repositorioMaisFamoso = repo.name();
                }
            }

            for (CompletableFuture<Map<String, Long>> future : futures) {

                Map<String, Long> languages = future.join();

                for (Map.Entry<String, Long> entry : languages.entrySet()) {
                    languageTotals.merge(
                            entry.getKey(),
                            entry.getValue(),
                            Long::sum
                    );
                }
            }

            page++;
        }

        List<Map.Entry<String, Long>> sortedLanguages =
                languageTotals.entrySet()
                        .stream()
                        .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                        .toList();

        List<String> topLanguages = sortedLanguages.stream()
                .limit(3)
                .map(Map.Entry::getKey)
                .toList();

        return new GithubRepositoryStats(
                totalStars,
                repositorioMaisFamoso,
                topLanguages
        );
    }

    private CompletableFuture<Map<String, Long>> getRepositoryLanguages(String name, String repositoryName) {

        return CompletableFuture.supplyAsync(() -> {

            String url = "https://api.github.com/repos/" + name
                    + "/" + repositoryName + "/languages";

            return restClient.get()
                    .uri(url)
                    .header("Authorization", "Bearer " + githubToken)
                    .retrieve()
                    //converter o json da resposta diretamente em um map
                    .body(new ParameterizedTypeReference<Map<String, Long>>() {
                    });
        });
    }
}



