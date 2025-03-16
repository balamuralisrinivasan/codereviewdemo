# Test Infrastructure

This directory contains the test infrastructure for the Inventory Management System. The tests are organized into the following categories:

## API Tests

Located in `java/com/example/inventorymanagement/api/`, these tests verify the behavior of the REST API endpoints using REST Assured and WebTestClient.

- `ProductApiTests.java`: Tests for the Product API using REST Assured
- `ProductWebClientTests.java`: Tests for the Product API using WebTestClient (for future reactive compatibility)

## Contract Tests

Located in `java/com/example/inventorymanagement/contract/` and `resources/contracts/`, these tests verify the API contracts using Spring Cloud Contract.

- `ProductContractBase.java`: Base class for Product API contract tests
- `contracts/products/shouldReturnProductById.groovy`: Contract definition for the Product API

## Performance Tests

Located in `java/com/example/inventorymanagement/performance/`, these tests measure the performance of the API endpoints using JMeter.

- `JMeterTestPlanGenerator.java`: Generates JMeter test plans for performance testing

## Test Data and Configuration

- `TestDataBuilder.java`: Creates test fixtures for API tests
- `TestConfig.java`: Configures the test environment
- `application-test.yml`: Configuration for the test environment

## Running the Tests

To run the API tests:

```bash
./src/test/resources/scripts/run-api-tests.sh
```

To run the contract tests:

```bash
mvn spring-cloud-contract:generateTests
mvn test -Dtest=com.example.inventorymanagement.contract.*
```

To run the performance tests:

```bash
mvn test -Dtest=com.example.inventorymanagement.performance.JMeterTestPlanGenerator
```

## Test Reports

Test reports are generated in the `target/test-reports` directory. 