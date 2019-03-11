[![Build Status](https://travis-ci.com/acmilanfan/blog-backend-kotlin.svg?branch=master)](https://travis-ci.com/acmilanfan/blog-backend-kotlin)
[![codecov](https://codecov.io/gh/acmilanfan/blog-backend-kotlin/branch/master/graph/badge.svg)](https://codecov.io/gh/acmilanfan/blog-backend-kotlin)

# Generic blog backend on Kotlin

**Current stack:**
* Spring Boot 2.1.2
* Kotlin 1.2.71
* Maven 3.5.4 (wrapper)
* Docker-maven-plugin 0.28.0 (fabric8)
* PostgreSQL 11.1
* Hibernate 5.3.7

# Build the project
`./mvnw package`

# Run tests
`./mvnw test`

After finishing the test you could see JaCoCo test coverage report in your target folder.

Example with chromium:

`chromium target/site/jacoco/index.html`

# Create docker image
`./mvnw docker:build`

# Run application in docker container
`./mvnw docker:run`