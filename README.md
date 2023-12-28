[![Merge Dependabot PR](https://github.com/derBobby/java-pretix-connector/actions/workflows/dependabot-automerge.yml/badge.svg)](https://github.com/derBobby/java-pretix-connector/actions/workflows/dependabot-automerge.yml) [![CD](https://github.com/derBobby/java-pretix-connector/actions/workflows/test-and-publish.yml/badge.svg)](https://github.com/derBobby/java-pretix-connector/actions/workflows/test-and-publish.yml)

# java-pretix-connector
This spring boot library allows to fetch almost everything related to Orders from the Pretix API.
Everything that was fetched from the Pretix API is cached/stored locally. Currently, there is no automatic refresh.
It can also handle WebHooks from Pretix.

## Features
* Fetch nearly everything order related
* Store fetched data locally
* Own data model, abstracting that of Pretix a bit for easier handling. Central classes are
  * `Booking` for everything that is booking related
  * `PretixEventFilter` A filter that can be used to search/filter within a list of Bookings
* Controller to handle Webhooks - implement Interface `IPretixWebHookHandler.class` on your side!

# Usage

## Maven setup
```xml
        <dependency>
            <groupId>eu.planlos</groupId>
            <artifactId>java-pretix-connector</artifactId>
            <version>1.0.0-SNAPSHOT</version>
        </dependency>
```

## Config classes setup
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

## Properties setup
| Property                                           | Type         | Description                                                                                                                                                                                                                                                                         |
|----------------------------------------------------|--------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `pretix.api.active`                                | Boolean      | Enable/Disable usage of API                                                                                                                                                                                                                                                         |
| `pretix.api.address`                               | String       | URL of the Pretix API                                                                                                                                                                                                                                                               |
| `pretix.api.token`                                 | String       | Authentication token                                                                                                                                                                                                                                                                | 
| `pretix.api.organizer`                             | String       | Organization of the owned events                                                                                                                                                                                                                                                    | 
| `pretix.api.retry-count`                           | Integer      | Retry count in case of exception                                                                                                                                                                                                                                                    | 
| `pretix.api.retry-interval`                        | Integer      | Interval for retries in case of exception                                                                                                                                                                                                                                           | 
| `pretix.api.event-list`                            | String       | List of events, that should be preloaded                                                                                                                                                                                                                                            | 
| `pretix.feature.preload-all-except-orders-enabled` | Boolean      | Preload everything but orders for events of `pretix.api.event-list`                                                                                                                                                                                                                 | 
| `pretix.feature.preload-orders-enabled`            | Boolean      | Preload orders for events of `pretix.api.event-list`                                                                                                                                                                                                                                | 
| `pretix.event-filter.source`                       | Enum         | `USER`, `PROPERTIES` (default): Defines source for filter, configured or set during runtime                                                                                                                                                                                         | 
| `pretix.event-filter.filter-list`                  | List of JSON | Format `{json}\|\|{json}` required due to the way Spring handles lists. Used to filter bookings if `pretix.event-filter.source` is set to `PROPERTIES`. List will be persisted in the database. If, on startup, filters exist in the database, then this property will not be used. |

### Filter Example
```json
{
  "action": "pretix.event.order.approved",
  "event": "zeltlager23ma",
  "qna-list": {
    "Zeltlager Jungs": [
      "Ja, die ganze Zeit",
      "Ja, nur tageweise (Siehe Sonstiges)",
      "Vielleicht"
    ]
  }
}
```

## Use in your code
Autowire the wanted services
```java
import eu.planlos.javapretixconnector.service.PretixBookingService;
import eu.planlos.javapretixconnector.service.PretixEventFilterService;

class YourClass {
    @Autowired
    private final PretixBookingService bookingService;
    @Autowired
    private final PretixEventFilterService filterService;
    // Your code
}
```

Call service to send message and catch Exception if necessary
```java
class YourClass {
    yourMethod() {
        try {
            signalService.sendMessageToAdmin("Hello Admin!");
            signalService.sendMessageToRecipients("Hello configured recipients!");
        } catch(PretixException e) {
            // your code
}   }   }
```
