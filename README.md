# Inventory Management System

A modern Spring Boot application for managing inventory and orders. This system allows businesses to track products, manage stock levels, and process customer orders efficiently.

## Features

- **Product Management**: Create, read, update, and delete products
- **Order Processing**: Create and manage customer orders
- **Inventory Tracking**: Automatically update inventory when orders are placed
- **Search Functionality**: Search products by name, category, and stock level
- **RESTful API**: Well-documented API endpoints for integration with other systems
- **OpenAPI Documentation**: Interactive API documentation with Swagger UI

## Technology Stack

- **Java 11**: LTS version of Java
- **Spring Boot 2.7.5**: Stable version of Spring Boot
- **Spring Data JPA**: For database operations
- **H2 Database**: In-memory database for development and testing
- **Lombok**: To reduce boilerplate code
- **SpringDoc OpenAPI**: For API documentation
- **Maven**: For dependency management and build
- **Allure**: For test reporting

## Getting Started

### Prerequisites

- Java 11 or higher
- Maven 3.6 or higher

### Running the Application

1. Clone the repository
2. Navigate to the project directory
3. Create a `settings.xml` file with the following content:
   ```xml
   <settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
     xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
     xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                         http://maven.apache.org/xsd/settings-1.0.0.xsd">
     <mirrors>
       <mirror>
         <id>central-mirror</id>
         <name>Maven Central</name>
         <url>https://repo.maven.apache.org/maven2</url>
         <mirrorOf>*</mirrorOf>
       </mirror>
     </mirrors>
   </settings>
   ```
4. Run the application using Maven:
   ```bash
   mvn -s settings.xml spring-boot:run
   ```

The application will start on port 8080 by default.

### Running Tests with Allure Reporting

The project uses Allure for test reporting. To run tests and generate Allure reports:

1. Run the tests with Allure reporting enabled:
   ```bash
   mvn clean test allure:report -s settings.xml
   ```

2. To view the Allure report, you can use one of the following methods:

   a. Using the Allure command-line tool (if installed):
   ```bash
   allure open target/allure-reports
   ```

   b. Using the downloaded Allure binary:
   ```bash
   .allure/allure-2.24.0/bin/allure open target/allure-reports
   ```

   c. Serve the report using a simple HTTP server:
   ```bash
   cd target/allure-reports && python -m http.server 8000
   ```
   Then open http://localhost:8000 in your browser.

The Allure report provides detailed test results, including test execution time, failures, and attachments.

### Accessing the Application

- **API Endpoints**: http://localhost:8080/api/
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **H2 Console**: http://localhost:8080/h2-console
  - JDBC URL: jdbc:h2:mem:inventorydb
  - Username: sa
  - Password: password

## API Endpoints

### Products

- `GET /api/products`: Get all products
- `GET /api/products/{id}`: Get product by ID
- `POST /api/products`: Create a new product
- `PUT /api/products/{id}`: Update an existing product
- `DELETE /api/products/{id}`: Delete a product
- `GET /api/products/search?name={name}`: Search products by name
- `GET /api/products/category/{category}`: Find products by category
- `GET /api/products/low-stock?threshold={threshold}`: Find products with low stock

### Orders

- `GET /api/orders`: Get all orders
- `GET /api/orders/{id}`: Get order by ID
- `POST /api/orders`: Create a new order
- `PUT /api/orders/{id}/status?status={status}`: Update order status
- `DELETE /api/orders/{id}`: Delete an order
- `GET /api/orders/status/{status}`: Find orders by status
- `GET /api/orders/customer/{email}`: Find orders by customer email
- `GET /api/orders/date-range?startDate={startDate}&endDate={endDate}`: Find orders by date range

## Example API Usage

### Creating a Product
```bash
curl -X POST -H "Content-Type: application/json" -d '{
  "name": "New Laptop",
  "description": "Latest model with high performance",
  "price": 1499.99,
  "quantity": 10,
  "category": "Electronics"
}' http://localhost:8080/api/products
```

### Creating an Order
```bash
curl -X POST -H "Content-Type: application/json" -d '{
  "customerName": "John Doe",
  "customerEmail": "john@example.com",
  "items": [
    {
      "product": {
        "id": 1
      },
      "quantity": 1
    }
  ]
}' http://localhost:8080/api/orders
```

### Updating Order Status
```bash
curl -X PUT "http://localhost:8080/api/orders/1/status?status=SHIPPED"
```

## Project Structure

```
src/main/java/com/example/inventorymanagement/
├── controller/           # REST controllers
├── model/                # Entity classes
├── repository/           # Spring Data JPA repositories
├── service/              # Business logic
├── exception/            # Exception handling
├── utils/                # Utility classes
└── InventoryManagementApplication.java  # Main class
```

## Implementation Notes

- The application uses public fields in entity classes instead of private fields with getters and setters for simplicity.
- JSON serialization for Order and OrderItem entities uses `@JsonManagedReference` and `@JsonBackReference` to handle circular references.
- Eager loading is used for the Product entity in OrderItem to avoid lazy loading exceptions.
- Constructor injection is used instead of field injection for better testability.

## Future Enhancements

- User authentication and authorization
- Payment processing integration
- Email notifications
- Reporting and analytics
- Multi-tenancy support

## License

This project is licensed under the MIT License - see the LICENSE file for details. 