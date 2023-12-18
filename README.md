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

## Properties

| Property                                           | Type    | Description                                                                       |
|----------------------------------------------------|---------|-----------------------------------------------------------------------------------|
| `pretix.api.active`                                | Boolean | Enable/Disable usage of API                                                       |
| `pretix.api.address`                               | String  | URL of the Pretix API                                                             |
| `pretix.api.token`                                 | String  | Authenticationtoken                                                               | 
| `pretix.api.organizer`                             | String  | Organization of the owned events                                                  | 
| `pretix.api.retry-count`                           | Integer | Retry count in case of exception                                                  | 
| `pretix.api.retry-interval`                        | Integer | Interval for retries in case of exception                                         | 
| `pretix.api.event-list`                            | String  | List of events, that should be preloaded                                          | 
| `pretix.feature.preload-all-except-orders-enabled` | Boolean | Preload everything but orders for events of `pretix.api.event-list`               | 
| `pretix.feature.preload-orders-enabled`            | Boolean | Preload orders for events of `pretix.api.event-list`                              | 
| `pretix.event-filter.source`                       | Enum    | `USER`, `PROPERTIES`: Defines source for filter, configured or set during runtime | 
| `pretix.event-filter.filter-list[]`                | JSON    | Used to filter bookings if `pretix.event-filter.source` is set to `PROPERTIES`    |

### Examples
`{"action": "pretix.event.order.approved", "event": "zeltlager23ma", "qna-list": {"Zeltlager Jungs":["Ja, die ganze Zeit","Ja, nur tageweise (Siehe Sonstiges)","Vielleicht"]}}`

## Status

[![Merge Dependabot PR](https://github.com/derBobby/java-pretix-connector/actions/workflows/dependabot-automerge.yml/badge.svg)](https://github.com/derBobby/java-pretix-connector/actions/workflows/dependabot-automerge.yml)

[![CD](https://github.com/derBobby/java-pretix-connector/actions/workflows/test-and-publish.yml/badge.svg)](https://github.com/derBobby/java-pretix-connector/actions/workflows/test-and-publish.yml)
