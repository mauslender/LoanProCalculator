# LoanProCalculatorTest

## Overview
This project is a test suite designed to uncover potential bugs in the LoanPro calculator application, which is run via a Docker container. The test suite is written in Java and uses the `ProcessBuilder` class to execute Docker commands.

## Requirements
- Java 8 or higher
- Maven
- Docker installed and running

## Project Structure
- `src/main/java/com/loanpro/LoanProCalculatorTest.java`: Main test class with various scenarios.
- `pom.xml`: Maven configuration file.
- `README.md`: This documentation file.

## How to Run the Tests
1. Clone the repository.
2. Navigate to the project directory.
3. Ensure Docker is running.
4. Run the tests using Maven:
   ```bash
   mvn test