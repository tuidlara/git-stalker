package com.arthur.github_analizer.service;

import com.arthur.github_analizer.dto.GithubRepositoryResponse;
import com.arthur.github_analizer.dto.GithubRepositoryStats;
import com.arthur.github_analizer.dto.GithubResponse;
import com.arthur.github_analizer.dto.GithubResponseApi;
import com.arthur.github_analizer.exception.GithubApiException;
import com.arthur.github_analizer.exception.GithubUserNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;

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
                    stats.mostPopularRepository()

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

            for (GithubRepositoryResponse repo : repos) {

                totalStars += repo.stargazersCount();

                if (repo.stargazersCount() > maiorNumeroDeEstrelas) {
                    maiorNumeroDeEstrelas = repo.stargazersCount();
                    repositorioMaisFamoso = repo.name();
                }
            }

            page++;
        }

        return new GithubRepositoryStats(
                totalStars,
                repositorioMaisFamoso
        );
    }
}



