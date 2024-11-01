# Getting Started

## Start db
````bash
docker-compose start db
````

## Build and start server
````bash
./mvnw spring-boot:run
````

## Build and run tests (requires db connection)
````bash
./mvnw clean install
````

## UI and API specs 
Navigate into http://localhost:8080/swagger-ui/index.html

## Build and start app and database via docker-compose
````bash
make start
````

## Run all tests
````bash
./mvnw test
````

