Demo project using Spring Data JPA, PostgreSQL in Docker, OpenAPI

DB setup:

`docker pull postgres` - download PostgreSQL image

`docker-compose up -d` - run compose file in /docker

`docker-compose stop` - stop container process

`docker ps` - shows running containers

API docs:

`localhost:8080/swagger` - visit to see Swagger (while app is running)

`./gradlew generateOpenApiDocs` - create OpenAPI json documentation
