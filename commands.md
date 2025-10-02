## Commands Scratchpad

```shell
docker run --name recoonnect-db -e POSTGRES_USER=user -e POSTGRES_PASSWORD=password -p 5432:5432 -d postgres:17
```

### Quick Start
```shell
# Start in development mode (file-based)
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Register a user
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username": "alice", "password": "password123", "email": "alice@example.com"}'

# Use the returned JWT token for all subsequent requests
curl -X GET http://localhost:8080/api/reconnect/contacts \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```


How to use it:
First, use the /api/auth/login endpoint in Swagger UI to authenticate with your username/password
Copy the JWT token from the response
Click the "Authorize" button in Swagger UI
Enter Bearer followed by your token (e.g., Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...)
Click "Authorize"
Now you can successfully call all protected API endpoints
The authentication endpoints (/api/auth/login and /api/auth/register) remain publicly accessible so you can get your initial JWT token, while all other endpoints will now properly accept the JWT token you provide through Swagger UI's authorization feature.