# Generic blog backend on Kotlin

**Current stack:**
* Spring Boot 2.1.2
* Kotlin 1.2.71
* Maven 3.5.4 (wrapper)
* Docker-maven-plugin 0.28.0 (fabric8)
* PostgreSQL 11.1
* Hibernate 5.3.7

**TODOs:**
* Implement liking a comment
* Implement disliking a comment
* Implement sorting comments by rating
* Write documentation (javaDocs)
* Test PostRepository.getAllByOrderByComments() method
* Wrap every exception in json with 5xx or 4xx responses codes (ControllerAdvice)
* ??? to be continued