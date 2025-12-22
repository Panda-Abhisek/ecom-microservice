
# File-Based Configuration Management with Config Server

## Refresh vs Profile Change – Execution Flows

----------

## Flow 1: **Configuration Value Change (NO Restart Required)**

### Scenario

-   Demo application is running with profile `dev`

-   Configuration files are stored in Config Server:

    `config/
     ├── demo-dev.yml
     └── demo-prod.yml`

-   Demo application:

    -   Uses `@RefreshScope`

    -   Has `spring-boot-starter-actuator`

    -   Exposes `/actuator/refresh`

    -   Fetches config from Config Server


----------

### Steps

1.  **Update configuration in Config Server**

    `# demo-dev.yml  build:  name:  "Updated Dev-Build"`

2.  **Call refresh endpoint on DEMO application**

    `curl -X POST http://localhost:8111/actuator/refresh`

3.  **What happens internally**

    -   Demo app calls Config Server again

    -   Config Server re-reads `demo-dev.yml`

    -   Updated values are returned

    -   Beans annotated with `@RefreshScope` are reinitialized

4.  **Result**

    -   Updated configuration is applied

    -   No restart required

    -   Application continues running


----------

### Key Notes

-   Only beans under `@RefreshScope` are refreshed

-   `/actuator/refresh` does **not** restart the application

-   Works for **value changes only**, not profile changes


----------

## Flow 2: **Environment / Profile Change (Restart REQUIRED)**

### Scenario

-   Demo application currently running with:

    `spring.profiles.active=dev`

-   You want to switch to:

    `spring.profiles.active=prod`


----------

### Steps

1.  **Change active profile**

    `-Dspring.profiles.active=prod`

    or via environment variable:

    `export SPRING_PROFILES_ACTIVE=prod`

2.  **Restart the demo application**

    `java -jar demo-application.jar`

3.  **What happens internally**

    -   Spring Boot re-evaluates active profile

    -   Config Server is queried for:

        `demo-prod.yml`

    -   Entire ApplicationContext is rebuilt

    -   Beans are created using `prod` configuration

4.  **Result**

    -   Application now runs with `prod` configuration

    -   New beans, new values, new behavior


----------

### Why Restart Is Mandatory

-   Active profile is resolved **only at startup**

-   `/actuator/refresh` cannot change profiles

-   Profile change affects:

    -   Bean creation

    -   Conditional configs

    -   Property resolution order


----------

## Quick Comparison
| Action | Requires Restart | Uses `/actuator/refresh` |
| ------ | ---------------- | ------------------------ |
| Config value change | ❌ No | ✅ Yes |
| Profile change (dev → prod) | ✅ Yes | ❌ No |


----------

## One-Line Summary (Interview-Ready)

> Configuration value changes can be applied dynamically using `/actuator/refresh`, but changing the active Spring profile always requires an application restart.