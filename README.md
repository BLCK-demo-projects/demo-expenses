Demo project using Spring Data JPA, PostgreSQL in Docker and OpenAPI. Basic React frontend consumes the API.

<img src="https://github.com/user-attachments/assets/bbe8e7b9-2283-46cb-819a-bfac5f9973fc" width="360"> 
<img src="https://github.com/user-attachments/assets/d3643837-3f54-49a2-b2f4-32336e0f4240" width="320">

DB setup:

`docker pull postgres` - download PostgreSQL image

`docker-compose up -d` - run compose file in /docker

`docker-compose stop` - stop container process

`docker ps` - shows running containers

API docs:

`localhost:8080/swagger` - visit to see Swagger (while app is running)

`./gradlew generateOpenApiDocs` - create OpenAPI json documentation

How to use:

Backend runs on 8080. `npm run dev` starts frontend on 5173.

Tests:

Backend has repository unit tests and API call "integration" tests.
