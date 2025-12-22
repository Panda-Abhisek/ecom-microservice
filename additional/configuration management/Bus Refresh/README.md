# Achieving **Actual Dynamic Updates** using

## Spring Cloud Config + Spring Cloud Bus (AMQP)

----------

## What “actual dynamic update” means

-   You change config **once**
    
-   You hit **one endpoint**
    
-   **All running instances** of all relevant services refresh automatically
    
-   No manual `/actuator/refresh` per service
    

That’s what **Spring Cloud Bus** is for.

----------

## Real-Life Analogy

Think of **RabbitMQ** as a **company-wide announcement system**.

-   Config Server announces: “Config changed!”
    
-   Every microservice hears it at the same time
    
-   Each service refreshes its own beans
    
-   Nobody runs around knocking on doors
    

----------

## High-Level Architecture

```
Config Files (Git / Native)
        ↓
Spring Cloud Config Server
        ↓
   Spring Cloud Bus
        ↓
RabbitMQ Exchange
        ↓
All Microservices

```

----------

## Prerequisites (Non-Negotiable)

You must already have:

-   Config Server working
    
-   `/actuator/refresh` working on **one** service
    
-   `@RefreshScope` used correctly
    
-   RabbitMQ running
    

If `/refresh` doesn’t work **without bus**, bus won’t magically fix it.

----------

## Step-by-Step Implementation

----------

## Step 1: Start RabbitMQ

Local (Docker is easiest):

```bash
docker run -d --name rabbitmq \
  -p 5672:5672 \
  -p 15672:15672 \
  rabbitmq:3-management

```

UI:

```
http://localhost:15672
username: guest
password: guest

```

----------

## Step 2: Add Dependencies

### Config Server

```xml
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-bus-amqp</artifactId>
</dependency>

```

### Demo Microservice

```xml
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-bus-amqp</artifactId>
</dependency>

```

Nothing else. No custom listeners required.

----------

## Step 3: RabbitMQ Configuration (Both Apps)

```yaml
spring:
  rabbitmq:
    host: localhost
    port: 5672
    username: guest
    password: guest

```

Yes, both **Config Server** and **all clients** must connect to RabbitMQ.

----------

## Step 4: Enable Actuator Bus Endpoint

### Config Server

```yaml
management:
  endpoints:
    web:
      exposure:
        include: busrefresh

```

You **do NOT** call `/refresh` anymore.

You call:

```
/actuator/busrefresh

```

----------

## Step 5: Client Configuration

### Demo application

```yaml
spring:
  application:
    name: demo
  config:
    import: optional:configserver:http://localhost:8222

```

And your bean:

```java
@RefreshScope
@RestController
public class DemoController {

  @Value("${app.message}")
  private String message;

  @GetMapping("/msg")
  public String message() {
    return message;
  }
}

```

----------

## Step 6: Change Configuration

Example:

```yaml
# demo-dev.yml
app:
  message: "Updated via Spring Cloud Bus"

```

Stored in Config Server backend.

----------

## Step 7: Trigger One Bus Event (This Is the Key)

Call **ONLY Config Server**:

```bash
curl -X POST http://localhost:8222/actuator/busrefresh

```

That’s it.

No service ports.  
No loops.  
No scripts.

----------

## What Happens Internally (Very Important)

1.  Config Server publishes a **RefreshRemoteApplicationEvent**
    
2.  Spring Cloud Bus sends it to RabbitMQ exchange
    
3.  RabbitMQ broadcasts to all subscribed services
    
4.  Each service:
    
    -   Calls Config Server
        
    -   Fetches updated config
        
    -   Refreshes `@RefreshScope` beans
        

All instances refresh **in parallel**.

----------

## What Gets Refreshed (and What Does NOT)

### Refreshed

-   `@RefreshScope` beans
    
-   `@Value` inside refresh-scoped beans
    
-   `@ConfigurationProperties` under refresh scope
    

### NOT Refreshed

-   Active profiles
    
-   Bean definitions
    
-   Datasource connections
    
-   Thread pools
    
-   Anything created at startup
    

Dynamic ≠ restart.

----------

## Targeted Refresh (Advanced but Useful)

Refresh only one service:

```bash
curl -X POST \
http://localhost:8888/actuator/busrefresh/demo

```

Refresh only `demo` service, not everything.

----------

## Why This Is the “Correct” Production Pattern

| Problem | Without Bus | With Bus |
| ------- | ----------- | -------- |
| Multiple instances | Manual | Automatic |
| Human error | High| Low|
| Scalability | Terrible| Excellent|
| Ops friendliness| No| Yes|

----------

## Common Mistakes (Read This Once)

-   Calling `/busrefresh` on **client** → ❌ wrong
    
-   Forgetting RabbitMQ on clients → ❌ no events received
    
-   Expecting profile change → ❌ restart required
    
-   No `@RefreshScope` → ❌ nothing updates
    

----------

## Interview One-Liner (Gold)

> Spring Cloud Bus uses a message broker like RabbitMQ to broadcast configuration change events so that all microservice instances refresh their configuration dynamically without manual intervention.

----------

## Final Truth

-   `/refresh` = local, manual, limited
    
-   `/busrefresh` = distributed, automated, production-grade

    
