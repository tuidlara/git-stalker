package com.arthur.github_analizer.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class GithubService {

    private final RestClient restClient;

    public GithubService(RestClient restClient) {
        this.restClient = restClient;
    }

    public String getGithubProfile(String name) {
        String url = "https://api.github.com/users/" + name;

        return restClient.get()
                .uri(url)
                .retrieve()
                .body(String.class);
    }
}
