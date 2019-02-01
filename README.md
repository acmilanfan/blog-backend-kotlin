# Generic blog backend on Kotlin

**Current stack:**
* Spring Boot 2.1.2
* Kotlin 1.2.71
* Maven 3.5.4 (wrapper)
* Docker-maven-plugin 0.28.0 (fabric8)
* PostgreSQL 11.1
* Hibernate 5.3.7

**TODOs:**
* Test /post/{id}/displayed method
* Add endpoint to get only displayed posts
* Implement liking a post
* Implement disliking a post
* Implement sorting posts by date
* Implement sorting posts by rating
* Implement the comment entity
* Add many to one relationship between comment and post
* Implement adding a new comment
* Implement deleting a comment
* Implement hiding/showing a comment (by default hidden)
* Implement sorting posts by comments number (most popular)
* Implement liking a comment
* Implement disliking a comment
* Implement sorting comments by rating
* ??? to be continued