package com.arthur.github_analizer.controller;

import com.arthur.github_analizer.dto.GithubResponse;
import com.arthur.github_analizer.service.GithubService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/github")
public class GithubController {

    private final GithubService githubService;

    public GithubController(GithubService githubService) {
        this.githubService = githubService;
    }

    @GetMapping("/{name}")
    public GithubResponse getUser(@PathVariable String name) {
        return githubService.getGithubProfile(name);
    }
}
