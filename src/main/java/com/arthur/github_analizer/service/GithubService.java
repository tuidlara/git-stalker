package com.arthur.github_analizer.service;

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

    private final RestClient restClient;

    public GithubService(RestClient restClient) {
        this.restClient = restClient;
    }

    public GithubResponse getGithubProfile(String name) {
        String url = "https://api.github.com/users/" + name;

        try {

            GithubResponseApi response = restClient.get()
                    .uri(url)
                    .retrieve()
                    .body(GithubResponseApi.class);

            if (response == null) {
                throw new GithubApiException("Github API returned an empty response");
            }

            return new GithubResponse(
                    response.login(),
                    response.name(),
                    response.company(),
                    response.location(),
                    response.bio(),
                    response.publicRepos(),
                    response.followers(),
                    response.following(),
                    response.createdAt()
            );
        }catch (HttpClientErrorException.NotFound e) {
            throw new GithubUserNotFoundException("Github not found");
        }catch (HttpServerErrorException e) {
            throw new GithubApiException("Github API is currently unavailable");
        }

    }

}
