# GitHub Analyzer

## Overview

GitHub Analyzer is a backend application built with **Java and Spring Boot** that analyzes GitHub profiles and their public repositories.

The application integrates with the **GitHub REST API**, processes the retrieved data, and provides useful statistics about a user's GitHub activity.

The project was developed to practice external API integration, authentication, DTOs, exception handling, pagination, data aggregation, and asynchronous processing with `CompletableFuture`.

## Features

* Retrieve GitHub profile information
* Retrieve public repository statistics
* Calculate total stars across repositories
* Identify the most popular repository
* Analyze programming languages used across repositories
* Return the user's top 3 programming languages
* Handle GitHub user not found errors
* Handle GitHub API errors
* Authenticate with GitHub using a personal access token
* Process repository language requests concurrently

## Technologies

* Java
* Spring Boot
* Spring Web
* RestClient
* GitHub REST API
* CompletableFuture
* Maven

## API

### Get GitHub Profile

```http
GET /github/{username}
```

Example:

```http
GET /github/tuidlara
```

The response includes:

* Username
* Name
* Company
* Location
* Bio
* Public repositories
* Followers
* Following
* Account creation date
* Total repository stars
* Most popular repository
* Top 3 programming languages

## GitHub Integration

The application communicates with GitHub to retrieve profile, repository, and language information.

Authentication is handled using a GitHub personal access token stored as an environment variable:

```text
GITHUB_TOKEN
```

The token is never stored directly in the source code.

## Asynchronous Processing

Language statistics require an additional request for each repository.

To avoid processing these requests strictly one at a time, the application uses `CompletableFuture` to execute multiple language requests concurrently.

This allows the application to analyze all repositories while reducing unnecessary waiting between requests.

## Project Structure

```text
src
├── config
├── controller
├── dto
├── exception
└── service
```

* **Controller** – Handles incoming requests.
* **Service** – Contains the analysis and GitHub integration logic.
* **DTOs** – Represent data exchanged between the application and GitHub.
* **Exception** – Handles API and application errors.
* **Config** – Configures the `RestClient`.

## Running the Project

### Clone the repository

```bash
git clone https://github.com/your-username/github-analyzer.git
cd github-analyzer
```

### Configure the GitHub token

Create an environment variable named:

```text
GITHUB_TOKEN
```

The application reads it using:

```java
System.getenv("GITHUB_TOKEN");
```

### Run the application

```bash
mvn spring-boot:run
```

Or:

```bash
./mvnw spring-boot:run
```

## Author

Arthur de Lara Zilli
