# Spring Boot Project Upgrade Plan

## Current State Analysis
- Spring Boot version: 2.7.5
- Java version: 11
- Key dependencies:
  - Spring Data JPA
  - Spring Web (Servlet-based)
  - H2 Database
  - Lombok
  - SpringDoc OpenAPI UI 1.6.12

## Upgrade Tasks

### 1. Core Platform Upgrades

#### 1.1 Update Spring Boot Version
```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.2.3</version>
    <relativePath/>
</parent>
```

#### 1.2 Upgrade Java Version
```xml
<properties>
    <java.version>17</java.version>
</properties>
```

#### 1.3 Update Dependencies with Version Changes
- Spring Doc OpenAPI needs to be updated to a version compatible with Spring Boot 3:
```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.3.0</version>
</dependency>
```

### 2. Migration to Reactive Stack

#### 2.1 Add WebFlux Dependencies
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webflux</artifactId>
</dependency>
```

#### 2.2 Add R2DBC Dependencies
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-r2dbc</artifactId>
</dependency>
<dependency>
    <groupId>io.r2dbc</groupId>
    <artifactId>r2dbc-h2</artifactId>
</dependency>
```

#### 2.3 Add Reactive Security
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

### 3. Observability Improvements

#### 3.1 Add Micrometer and Prometheus
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-registry-prometheus</artifactId>
</dependency>
```

#### 3.2 Add Distributed Tracing
```xml
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-tracing-bridge-brave</artifactId>
</dependency>
<dependency>
    <groupId>io.zipkin.reporter2</groupId>
    <artifactId>zipkin-reporter-brave</artifactId>
</dependency>
```

### 4. Complete Updated POM File

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.3</version>
        <relativePath/>
    </parent>
    <groupId>com.example</groupId>
    <artifactId>inventory-management</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>inventory-management</name>
    <description>Inventory Management System</description>
    <properties>
        <java.version>17</java.version>
    </properties>
    <dependencies>
        <!-- Data Access -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-r2dbc</artifactId>
        </dependency>
        <dependency>
            <groupId>io.r2dbc</groupId>
            <artifactId>r2dbc-h2</artifactId>
        </dependency>
        
        <!-- Keep JPA for migration period -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
            <scope>runtime</scope>
        </dependency>
        
        <!-- Web & API -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-webflux</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springdoc</groupId>
            <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
            <version>2.3.0</version>
        </dependency>
        
        <!-- Security -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
        
        <!-- Observability -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <dependency>
            <groupId>io.micrometer</groupId>
            <artifactId>micrometer-registry-prometheus</artifactId>
        </dependency>
        <dependency>
            <groupId>io.micrometer</groupId>
            <artifactId>micrometer-tracing-bridge-brave</artifactId>
        </dependency>
        <dependency>
            <groupId>io.zipkin.reporter2</groupId>
            <artifactId>zipkin-reporter-brave</artifactId>
        </dependency>
        
        <!-- Dev Tools -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>io.projectreactor</groupId>
            <artifactId>reactor-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </exclude>
                    </excludes>
                </configuration>
            </plugin>
        </plugins>
    </build>
    
    <repositories>
        <repository>
            <id>central</id>
            <name>Maven Central</name>
            <url>https://repo.maven.apache.org/maven2</url>
        </repository>
    </repositories>
</project>
```

## Code Migration Tasks

### 1. Jakarta EE Migration
- Update imports from `javax.*` to `jakarta.*` packages
- Example:
  ```java
  // Before
  import javax.persistence.Entity;
  
  // After
  import jakarta.persistence.Entity;
  ```

### 2. Implement Java 17 Features

#### 2.1 Records for DTOs
```java
// Before
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private String id;
    private String name;
    private BigDecimal price;
}

// After
public record ProductDTO(String id, String name, BigDecimal price) {}
```

#### 2.2 Pattern Matching for instanceof
```java
// Before
if (obj instanceof Product) {
    Product product = (Product) obj;
    return product.getName();
}

// After
if (obj instanceof Product product) {
    return product.getName();
}
```

#### 2.3 Switch Expressions
```java
// Before
String status;
switch (product.getCategory()) {
    case "ELECTRONICS":
        status = "High Priority";
        break;
    case "FOOD":
        status = "Perishable";
        break;
    default:
        status = "Standard";
}

// After
String status = switch (product.getCategory()) {
    case "ELECTRONICS" -> "High Priority";
    case "FOOD" -> "Perishable";
    default -> "Standard";
};
```

#### 2.4 Text Blocks for SQL/JSON
```java
// Before
String query = "SELECT p.id, p.name, p.price " +
               "FROM products p " +
               "WHERE p.category = :category " +
               "ORDER BY p.name";

// After
String query = """
    SELECT p.id, p.name, p.price
    FROM products p
    WHERE p.category = :category
    ORDER BY p.name
    """;
```

#### 2.5 Sealed Classes
```java
public sealed abstract class InventoryItem
    permits Product, Equipment, RawMaterial {
    
    // Common properties/methods
}

public final class Product extends InventoryItem {
    // Product-specific implementation
}

public final class Equipment extends InventoryItem {
    // Equipment-specific implementation
}

public non-sealed class RawMaterial extends InventoryItem {
    // RawMaterial-specific implementation
}
```

### 3. Reactive Programming Implementation

#### 3.1 Convert JPA Repositories to R2DBC
```java
// Before
@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    List<Product> findByCategory(String category);
    Optional<Product> findByName(String name);
}

// After
@Repository
public interface ProductRepository extends ReactiveCrudRepository<Product, String> {
    Flux<Product> findByCategory(String category);
    Mono<Product> findByName(String name);
}
```

#### 3.2 Update Entity Classes
```java
// Before
@Entity
@Table(name = "products")
public class Product {
    @Id
    private String id;
    // other fields
}

// After
@Table("products")
public class Product {
    @Id
    private String id;
    // other fields, no @Entity needed for R2DBC
}
```

#### 3.3 Convert REST Controllers to Reactive
```java
// Before
@RestController
@RequestMapping("/api/products")
public class ProductController {
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable String id) {
        // Implementation
    }
    
    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) {
        // Implementation
    }
}

// After
@RestController
@RequestMapping("/api/products")
public class ProductController {
    @GetMapping("/{id}")
    public Mono<ResponseEntity<ProductDTO>> getProduct(@PathVariable String id) {
        // Reactive implementation
    }
    
    @PostMapping
    public Mono<ResponseEntity<ProductDTO>> createProduct(@RequestBody ProductDTO productDTO) {
        // Reactive implementation
    }
}
```

#### 3.4 Implement Reactive Service Layer
```java
// Before
@Service
public class ProductService {
    public ProductDTO findById(String id) {
        // Implementation
    }
}

// After
@Service
public class ProductService {
    public Mono<ProductDTO> findById(String id) {
        // Reactive implementation
    }
}
```

### 4. Observability Implementation

#### 4.1 Configure Actuator Endpoints
```yaml
# application.yml
management:
  endpoints:
    web:
      exposure:
        include: health,info,prometheus,metrics
  endpoint:
    health:
      show-details: always
  metrics:
    tags:
      application: ${spring.application.name}
  tracing:
    sampling:
      probability: 1.0
```

#### 4.2 Add Custom Metrics
```java
@Component
public class ProductMetrics {
    private final MeterRegistry meterRegistry;
    
    public ProductMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        
        // Register gauges, counters, etc.
        meterRegistry.gauge("inventory.product.count", 
            Tags.of("type", "total"), 
            this, 
            pm -> productRepository.count().block());
    }
    
    public void recordProductAccess(String productId) {
        meterRegistry.counter("inventory.product.access", 
            "productId", productId).increment();
    }
}
```

## Implementation Strategy

### Phase 0: Test Infrastructure Setup
1. Set up automated API test suite
   - Add REST Assured for testing current servlet-based endpoints
   ```xml
   <dependency>
       <groupId>io.rest-assured</groupId>
       <artifactId>rest-assured</artifactId>
       <scope>test</scope>
   </dependency>
   <dependency>
       <groupId>io.rest-assured</groupId>
       <artifactId>json-path</artifactId>
       <scope>test</scope>
   </dependency>
   <dependency>
       <groupId>io.rest-assured</groupId>
       <artifactId>xml-path</artifactId>
       <scope>test</scope>
   </dependency>
   ```

2. Create test fixtures and data setup
   - Implement test data builders
   - Set up database initialization scripts for tests
   - Create test configuration for in-memory database

3. Implement comprehensive API tests
   ```java
   @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
   public class ProductApiTests {
       @LocalServerPort
       private int port;
       
       @BeforeEach
       public void setup() {
           RestAssured.port = port;
           // Initialize test data
       }
       
       @Test
       public void testGetProduct() {
           // Test GET endpoint 
           given()
               .when()
               .get("/api/products/{id}", "test-id")
               .then()
               .statusCode(200)
               .body("id", equalTo("test-id"))
               .body("name", notNullValue());
       }
       
       // Additional tests for POST, PUT, DELETE, etc.
   }
   ```

4. Set up contract testing with Spring Cloud Contract
   ```xml
   <dependency>
       <groupId>org.springframework.cloud</groupId>
       <artifactId>spring-cloud-starter-contract-verifier</artifactId>
       <scope>test</scope>
   </dependency>
   ```

5. Prepare for future reactive testing
   ```xml
   <dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-webflux</artifactId>
       <scope>test</scope>
   </dependency>
   ```
   ```java
   // Example test that will work with both servlet and reactive endpoints
   @Test
   public void testGetProductWithWebTestClient() {
       webTestClient
           .get()
           .uri("/api/products/{id}", "test-id")
           .exchange()
           .expectStatus().isOk()
           .expectBody()
           .jsonPath("$.id").isEqualTo("test-id")
           .jsonPath("$.name").isNotEmpty();
   }
   ```

6. Implement performance benchmarks
   - Set up JMeter test plans for critical API endpoints
   - Define performance SLAs to maintain through upgrade
   - Create scripts to compare performance before and after each phase

### Phase 1: Core Infrastructure Upgrade
1. Upgrade Spring Boot to 3.2.3
2. Upgrade to Java 17
3. Update dependencies and fix Jakarta EE imports
4. Implement comprehensive unit tests to verify functionality

### Phase 2: Modern Java Features
1. Implement Records for DTOs
2. Update code to use pattern matching and switch expressions
3. Refactor for sealed classes and text blocks

### Phase 3: Reactive Programming
1. Implement parallel architecture (maintain both JPA and R2DBC initially)
2. Convert repositories, services, and controllers to reactive
3. Add WebFlux routes alongside traditional controllers
4. Implement reactive security

### Phase 4: Observability
1. Configure actuator endpoints and Prometheus integration
2. Implement custom metrics for business processes
3. Configure distributed tracing
4. Create dashboards for monitoring

## Testing Strategy

### 1. Unit Testing
- Update test dependencies to include reactive test support
- Use StepVerifier for testing Mono and Flux responses
- Test both synchronous and reactive components during migration

### 2. Integration Testing
- Use WebTestClient instead of MockMvc for testing reactive endpoints
- Implement reactive database tests with TestContainers
- Test WebFlux security configurations

### 3. Performance Testing
- Benchmark reactive vs. non-reactive endpoints
- Validate memory usage improvements
- Test concurrent user scenarios

## Deployment Considerations

### 1. Database Migration
- Run JPA and R2DBC in parallel during transition
- Consider data migration strategy for schema changes

### 2. Monitoring Setup
- Deploy Prometheus and Grafana for metrics visualization
- Configure alerting based on new metrics
- Implement distributed tracing with Zipkin or Jaeger

### 3. Documentation
- Update API documentation with OpenAPI 3.0
- Document new reactive endpoints and their behavior
- Create developer guide for reactive programming patterns