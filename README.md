# Meilisearch Rest Client

[Meilisearch](https://github.com/meilisearch/meilisearch) is a lightning-fast search engine that fits effortlessly into your apps, websites, and workflow.

> The goal of this library is to provide a `object-oriented`, `semantic`, `reactive`, and `streamlined` meilisearch-rest-client, supporting reactivity through reactor-netty-http.

### Version
The version number of this library is named by appending `.X` to the version number in the official documentation.

- v1.5 Docs: [1.5.0.x,) (current)

### Dependencies

*By default,  this library depends on libraries as fallow:*

- reactor-netty-http(required)
- jackson(replaceable by provide implementation of `io.github.honhimw.ms.JsonHandler`)

## Installation

**Note**: not yet deploy

```shell
# build from sources
$ gradle clean build -x test
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
public static void main(String[]args) {
    JsonHandler jsonHandler = new JacksonJsonHandler();
    ReactiveMSearchClient client = ReactiveMSearchClient.create(builder -> builder
        .serverUrl("http://{{meilisearch-server-host}}")
        .jsonHandler(jsonHandler)
    );

    MSearchClient blockingClient = MSearchClient.create(builder -> builder
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

## Run Tests

```shell
$ gradle test
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
