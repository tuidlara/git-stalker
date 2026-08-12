package com.arthur.github_analizer.service;

import com.arthur.github_analizer.dto.GithubResponse;
import com.arthur.github_analizer.dto.GithubResponseApi;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class GithubService {

    private final RestClient restClient;

    public GithubService(RestClient restClient) {
        this.restClient = restClient;
    }

    public GithubResponse getGithubProfile(String name) {
        String url = "https://api.github.com/users/" + name;

        GithubResponseApi response = restClient.get()
                .uri(url)
                .retrieve()
                .body(GithubResponseApi.class);

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
    }


}
