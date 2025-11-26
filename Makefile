
help:
	@echo "ClusteredData Warehouse - Available Commands:"
	@echo ""
	@echo "  make build          - Build the application using Maven"
	@echo "  make run            - Start the application with Docker Compose"
	@echo "  make stop           - Stop all running containers"
	@echo "  make test           - Run all tests with coverage report"
	@echo "  make logs           - View application logs"
	@echo "  make restart        - Restart the application"
	@echo ""

# Build the application
build:
	@echo "Building application..."
	mvn clean package -DskipTests
	@echo "Build complete!"

# Start the application with Docker Compose
run:
	@echo "Starting the app..."
	docker-compose up -d
	@echo "App started!"
	@echo "Application: http://localhost:8080"

# Stop the app
stop:
	@echo "Stopping app..."
	docker-compose down
	@echo "app stopped!"

# Clean up app
clean:
	@echo "Cleaning up..."
	docker-compose down -v
	@echo "Cleanup complete!"

# Run tests
test:
	@echo "Running tests..."
	mvn test
	@echo "Tests complete!"
	@echo "Coverage report: target/site/jacoco/index.html"

# View logs
logs:
	docker-compose logs -f app

# Open database shell
db-shell:
	@echo "Opening PostgreSQL shell..."
	docker-compose exec postgres psql -U postgres -d fxdeals

# Restart the application
restart: stop run

# Build and run
up: build run

# Full rebuild
rebuild: clean build run