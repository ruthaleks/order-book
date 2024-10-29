run:
	docker-compose down -v
	./mvnw clean install -DskipTests
	docker build -t order-book:latest .
	docker-compose up -d

test:
	docker-compose down -v
	./mvnw clean install -DskipTests
	docker build -t order-book:latest .
	docker-compose up -d
	./mvnw test
