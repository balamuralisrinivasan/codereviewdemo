#!/bin/bash

# Script to run API tests and generate a report

# Set variables
TEST_DIR="target/test-results"
REPORT_DIR="target/test-reports"

# Create directories
mkdir -p $TEST_DIR
mkdir -p $REPORT_DIR

# Run the tests
echo "Running API tests..."
mvn test -Dtest=com.example.inventorymanagement.api.* -Dspring.profiles.active=test

# Generate the report
echo "Generating test report..."
mvn surefire-report:report-only

echo "Tests completed. Report available at $REPORT_DIR" 