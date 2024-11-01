stop:
	docker-compose down -v

start:
	./mvnw clean install -DskipTests
	docker build -t order-book:latest .
	docker-compose up -d
