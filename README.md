# java-pretix-connector
This spring boot library allows to connect to the Pretix API.
It can also handle WebHooks from Pretix.

## Features
### Users endpoint
* Fetch ...
* Fetch ...
* Fetch ...
* Create ...

## Usage
Add Maven dependency
```xml
        <dependency>
            <groupId>eu.planlos</groupId>
            <artifactId>java-pretix-connector</artifactId>
            <version>1.0.0-SNAPSHOT</version>
        </dependency>
```

Add Configuration class
```java
@Configuration
@ComponentScan(basePackages = "eu.planlos.javapretixconnector")
public class PretixConfig {}
```

Add Configuration class with `@EntityScan` and `@EnableJpaRepositories` for both, your package and the library:
```java
@Configuration
@EnableJpaRepositories(basePackages = {"com.package.your", "eu.planlos.javapretixconnector"})
@EntityScan(basePackages = {"com.package.your", "eu.planlos.javapretixconnector"})
public class DataConfig {}
```

## Status

[![Merge Dependabot PR](https://github.com/derBobby/java-pretix-connector/actions/workflows/dependabot-automerge.yml/badge.svg)](https://github.com/derBobby/java-pretix-connector/actions/workflows/dependabot-automerge.yml)

[![CD](https://github.com/derBobby/java-pretix-connector/actions/workflows/test-and-publish.yml/badge.svg)](https://github.com/derBobby/java-pretix-connector/actions/workflows/test-and-publish.yml)
