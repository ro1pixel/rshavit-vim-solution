# User Notifications Manager

A Spring Boot-based microservice for managing user notification preferences and sending notifications. This service integrates with an external notification service to handle email and SMS notifications with rate limiting capabilities.

## Features

- User notification preference management
- Integration with external notification service
- Rate-limited email and SMS notifications
- RESTful API endpoints
- Spring Security integration
- Docker containerization support

## Tech Stack

- Java 17
- Spring Boot 3.2.3
- Spring Security
- Spring Validation
- Lombok
- Docker
- Docker Compose

## Prerequisites

- Java 17 or higher
- Maven
- Docker and Docker Compose

## Getting Started

### Local Development

1. Clone the repository:

   ```bash
   git clone <repository-url>
   cd user-notifications-manager
   ```

2. Build the project:

   ```bash
   mvn clean install
   ```

3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

### Docker Deployment

1. Build and start the services using Docker Compose:
   ```bash
   docker-compose up --build
   ```

The application will be available at:

- User Notifications Manager: http://localhost:8080
- Notification Service: http://localhost:5001

## API Documentation

The service provides the following endpoints:

- `POST /api/v1/users/{userId}/preferences` - Set user notification preferences
- `GET /api/v1/users/{userId}/preferences` - Get user notification preferences
- `POST /api/v1/users/{userId}/notifications` - Send notifications to a user

## Environment Variables

### User Notifications Manager

- `NOTIFICATION_SERVICE_URL` - URL of the notification service (default: http://notification-service:5001)

### Notification Service

- `EMAIL_RATE_LIMIT` - Maximum number of emails per time window (default: 5)
- `SMS_RATE_LIMIT` - Maximum number of SMS per time window (default: 5)
- `RATE_LIMIT_WINDOW_MS` - Time window for rate limiting in milliseconds (default: 1000)
- `ERROR_RATE` - Simulated error rate for testing (default: 0)

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/vim/
│   │       ├── controller/
│   │       ├── service/
│   │       ├── model/
│   │       ├── repository/
│   │       └── config/
│   └── resources/
│       └── application.properties
└── test/
    └── java/
        └── com/vim/
```

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.
