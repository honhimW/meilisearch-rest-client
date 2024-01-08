# Meilisearch Rest Client

[Meilisearch](https://github.com/meilisearch/meilisearch) is a lightning-fast search engine that fits effortlessly into your apps, websites, and workflow.

> The goal of this library is to provide a more `object-oriented` and `semantic` meilisearch-rest client, supporting reactive usage through reactor-http.

### Version
this library version naming as the official docs api version append `.X`

- v1.5 Docs: [1.5.0.x,) (current)

### Dependencies

*by default this library depend on library as fallow:*

- reactor-netty-http(required)
- jackson(replaceable by provide implementation of `io.github.honhimw.ms.JsonHandler`)

## Installation

> Note: not yet deploy

```shell
# build from sources
$ gradle clean build
```

```groovy
// Gradle
dependencies {
    implementation 'io.github.honhimw:meilisearch-rest-client:1.5.0.0-SNAPSHOT'
}
```

```xml
<!-- Maven -->
<dependency>
    <groupId>io.github.honhimw</groupId>
    <artifactId>meilisearch-rest-client</artifactId>
    <version>1.5.0.0-SNAPSHOT</version>
</dependency>
```

## Usage

```java
public static void main(String[]args){
    JsonHandler jsonHandler = new JacksonJsonHandler();
    ReactiveMSearchClient client = ReactiveMSearchClient.create(builder -> builder
        .serverUrl("http://{{meilisearch-server-host}}")
        .jsonHandler(jsonHandler)
    );

    MSearchClient blockingClien = MSearchClient.create(builder -> builder
            .serverUrl("http://{{meilisearch-server-host}}")
            .jsonHandler(jsonHandler)
        );

    Mono<SearchResponse<Movie>> searchResponse = client.indexes(indexes -> indexes
        .search("movies", reactiveSearch -> reactiveSearch
            .find("hello world", Movie.class)));
    List<Movie> hits = searchResponse.block().getHits();
    // or
    Mono<SearchResponse<Movie>> searchResponse2 = client.indexes(indexes1 -> indexes1
        .search(index, Movie.class, search -> search
            .find("hello world")));
    List<Movie> hits2 = searchResponse2.block().getHits();
}
```