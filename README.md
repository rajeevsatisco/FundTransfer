# Fund Transfer API

## Overview

The Fund Transfer API is a RESTful web service implemented in Java that allows for transferring funds between two accounts with currency exchange. The project aims to demonstrate a basic, yet robust implementation of fund transfers with features such as concurrency support, external currency exchange rates retrieval, and error handling.
## Requirements

- Language/Framework: Java 8 +, Springboot,JPA, Hibernate, H2, Mockito, Junit etc.
- Maven
- External API for currency exchange rates (e.g., [ExchangeRate-API](https://api.fastforex.io/fetch-multi?from=EUR&to=INR&api_key=4b0137d326-2edd264b5a-sgky0z))
## Features

- Supports concurrent invocation by multiple users/systems.
- Defined minimal attributes for an Account: Owner ID (numeric), Currency (String), and Balance (numeric).
- Retrieves exchange rates from external APIs.
- Includes conditions to handle fund transfer failures.
- Developed with a focus on maintainability and code quality.
- Functionality is covered with comprehensive tests.
## OpenAPI Specifications
    http://localhost:8080/swagger-ui/index.html#/todo-controller/getTodo
## Account REST API Endpoints
- POST: http://localhost:8080/accounts
- GET : http://localhost:8080/accounts
- GET : http://localhost:8080/accounts/{accountId}
- PUT : http://localhost:8080/accounts/{accountId}
- PUT : http://localhost:8080/activate
- PUT : http://localhost:8080/deactivate
- POST: http://localhost:8080/fund-transfer
- GET : http://localhost:8080/fund-transfer
## ExternalRate API URL
    https://api.fastforex.io/fetch-multi?from=EUR&to=INR&api_key=4b0137d326-2edd264b5a-sgky0z
## Database
    http://localhost:8080/h2-console/
### Run the application
    mvn spring-boot: run
### Entity (Table)
    Account
    FundTransfer
### Clone the Repository

```bash
git clone https://github.com/rajeevsatisco/FundTransfer.git
cd FundTransfer

