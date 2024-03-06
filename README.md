# Meilisearch Rest Client

[![License](https://img.shields.io/badge/license-Apache2-blue.svg)](https://github.com/honhimW/meilisearch-rest-client?tab=Apache-2.0-1-ov-file#readme)
[![Maven](https://img.shields.io/maven-central/v/io.github.honhimw/meilisearch-rest-client)](https://central.sonatype.com/artifact/io.github.honhimw/meilisearch-rest-client)
[![CI](https://github.com/honhimW/meilisearch-rest-client/actions/workflows/gradle.yml/badge.svg)](https://github.com/honhimW/meilisearch-rest-client/actions)
[![Codecov](https://img.shields.io/codecov/c/github/honhimw/meilisearch-rest-client)](https://app.codecov.io/github/honhimW/meilisearch-rest-client)


---

<h4 align="center">
  <a href="https://github.com/meilisearch/documentation">Doc Repository</a> |
  <a href="https://www.meilisearch.com/docs">Documentation</a>
</h4>

[Meilisearch](https://github.com/meilisearch/meilisearch) is a lightning-fast search engine that fits effortlessly into your apps, websites, and workflow.

> The goal of this library is to provide a `object-oriented`, `semantic`, `reactive`, and `streamlined` meilisearch-rest-client, supporting reactivity through reactor-netty-http.

### Version
The version number of this library is named by appending `.X` to the version number in the official documentation.

- v1.7 Docs: [1.7.0.x,) (*current)
- v1.6 Docs: [1.6.0.x, 1.7.0.0)
- v1.5 Docs: [1.5.0.x, 1.6.0.0) 

### Dependencies

*By default, this library depends on libraries as fallows:*

- reactor-netty-http(required)
- jackson(replaceable by providing implementation of `io.github.honhimw.ms.JsonHandler`)

## Installation

```shell
# build from sources
$ gradlew clean build -x test
```

```groovy
// Gradle
implementation 'io.github.honhimw:meilisearch-rest-client:1.7.0.0-RC.1'
```

```xml
<!-- Maven -->
<dependency>
    <groupId>io.github.honhimw</groupId>
    <artifactId>meilisearch-rest-client</artifactId>
    <version>1.7.0.0-RC.1</version>
</dependency>
```

**[Copy Snippets here.](https://central.sonatype.com/artifact/io.github.honhimw/meilisearch-rest-client)**

## Usage

#### Reactive(reactor)
```java
public static void main(String[] args) {
    JsonHandler jsonHandler = new JacksonJsonHandler();
    try (
        ReactiveMSearchClient client = ReactiveMSearchClient.create(builder -> builder
            .enableSSL(false)                    // true: https, false: http
            .host("{{meilisearch-server-host}}") // server host
            .port(7700)                          // server port
            .jsonHandler(jsonHandler)
            .httpClient(ReactiveHttpUtils.getInstance(http -> http.readTimeout(Duration.ofMillis(100)))))
    ) {
        String indexUid = "movies";
        Mono<SearchResponse<Movie>> searchResponse = client.indexes(indexes -> indexes
            .search(indexUid, search -> search
                .find("hello world", Movie.class)));
        List<Movie> hits = searchResponse.block().getHits();
        // or
        List<Movie> hits2 = client.indexes(indexes -> indexes
            .search(indexUid, Movie.class, search -> search
                .find(q -> q
                    .q("hello world")
                    .limit(1)
                )
                .map(SearchResponse::getHits)
            )
        ).block();
    }
}
```

#### Blocking
```java
public static void main(String[] args) {
    JsonHandler jsonHandler = new JacksonJsonHandler();
    try (
        MSearchClient client = MSearchClient.create(builder -> builder
            .enableSSL(false)                    // true: https, false: http
            .host("{{meilisearch-server-host}}") // server host
            .port(7700)                          // server port
            .jsonHandler(jsonHandler))
            .httpClient(ReactiveHttpUtils.getInstance(http -> http.readTimeout(Duration.ofMillis(100)))))
    ) {
        String indexUid = "movies";
        SearchResponse<Movie> searchResponse = client.indexes(indexes -> indexes
            .search(indexUid, search -> search
                .find("hello world", Movie.class)));
        List<Movie> hits = searchResponse.getHits();
        // or
        List<Movie> hits2 = client.indexes(indexes -> indexes
            .search(indexUid, Movie.class, search -> search
                .find(q -> q
                    .q("hello world")
                    .limit(1)
                )
                .getHits()
            )
        );
    }
}
```

## Run Tests

```shell
$ gradlew test
```

Create file named `profile-test.properties` under project root directory.

```properties
./meilisearch-rest-client
└── profile-test.properties

meili-search.host=127.0.0.1
meili-search.port=7700
meili-search.api-key=
```

**Note**: You may also set `profiles.active` in gradle.properties for loading different properties file such as:  
> profile-alpha.properties: by setting profiles.active=alpha  
> profile-beta.properties: by setting profiles.active=beta
