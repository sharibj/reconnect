# Multi-Tenant Reconnect Application

This application now supports multi-tenancy with user authentication and authorization using Spring Security and JWT tokens.

## Features

- **Multi-Tenant Data Isolation**: Each user's data is completely isolated from other users
- **JWT Authentication**: Secure token-based authentication
- **Dual Storage Support**: Works with both file-based storage (dev) and PostgreSQL (prod)
- **RESTful API**: Full REST API with Swagger documentation

## Quick Start

### 1. Register a New User
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "password": "password123",
    "email": "john@example.com"
  }'
```

Response:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "john_doe",
  "message": "User registered successfully"
}
```

### 2. Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "password": "password123"
  }'
```

### 3. Use Protected Endpoints
Include the JWT token in the Authorization header:

```bash
curl -X GET http://localhost:8080/api/reconnect/contacts \
  -H "Authorization: Bearer YOUR_JWT_TOKEN_HERE"
```

## Multi-Tenant Architecture

### File-Based Storage (Dev Profile)
- Each user gets their own folder: `data/tenants/{username}/`
- Files are isolated: `contacts.txt`, `groups.txt`, `interactions.txt`
- Automatic directory creation on first use

### Database Storage (Prod Profile)
- Each table includes a `username` column for tenant isolation
- All queries are automatically filtered by the current user's username
- Foreign key relationships maintain tenant boundaries

## Database Setup (Production)

1. Run the migration script to add multi-tenant columns:
```bash
psql -h localhost -U postgres -d reconnect -f migration.sql
```

2. Set environment variables:
```bash
export DB_URL=localhost
export DB_NAME=reconnect
export DB_USERNAME=your_db_user
export DB_PASSWORD=your_db_password
export JWT_SECRET=your_secure_random_secret_key_here
```

## Running the Application

### Development Mode (File Storage)
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### Production Mode (Database Storage)
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

## API Documentation

Visit http://localhost:8080/swagger-ui.html for interactive API documentation.

### Authentication Endpoints
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - Login user

### Protected Endpoints (Require JWT Token)
- `GET /api/reconnect/contacts` - Get user's contacts
- `POST /api/reconnect/contacts` - Create contact
- `PUT /api/reconnect/contacts/{nickName}` - Update contact
- `DELETE /api/reconnect/contacts/{nickName}` - Delete contact
- Similar endpoints for groups and interactions

## Security Features

- **Password Encryption**: BCrypt hashing for all passwords
- **JWT Tokens**: Secure, stateless authentication
- **CORS Support**: Configurable cross-origin resource sharing
- **Tenant Isolation**: Complete data separation between users
- **Auto-cleanup**: Tenant context automatically cleared after requests

## Data Isolation Examples

User "alice" creates a contact:
```json
{
  "nickName": "Bob",
  "group": "Work"
}
```

User "bob" cannot see Alice's contacts - they are completely isolated.

### File System Structure
```
data/
├── tenants/
│   ├── alice/
│   │   ├── contacts.txt
│   │   ├── groups.txt
│   │   └── interactions.txt
│   └── bob/
│       ├── contacts.txt
│       ├── groups.txt
│       └── interactions.txt
```

### Database Structure
```sql
-- All queries automatically include username filter
SELECT * FROM contacts WHERE username = 'alice';
SELECT * FROM groups WHERE username = 'alice';
SELECT * FROM interactions WHERE username = 'alice';
```

## Configuration

### JWT Settings
- `app.jwt.secret`: Secret key for JWT signing (change in production!)
- `app.jwt.expiration`: Token expiration time in milliseconds (default: 24 hours)

### Security
- All endpoints except `/api/auth/**` require authentication
- Swagger UI and health checks are publicly accessible
- CORS is configured to allow all origins (configure for production)

## Testing

Run the test suite:
```bash
mvn test
```

The application includes comprehensive tests for:
- Multi-tenant data isolation
- Authentication flows
- Repository operations
- Service layer logic

## Production Deployment

1. Set secure JWT secret: `export JWT_SECRET=$(openssl rand -base64 64)`
2. Configure database connection
3. Run migration script
4. Deploy with prod profile: `java -jar app.jar --spring.profiles.active=prod`

## Troubleshooting

### Common Issues

1. **"No tenant context available"**: Ensure JWT token is included in Authorization header
2. **Database connection errors**: Check database URL, credentials, and network connectivity
3. **JWT validation fails**: Verify token hasn't expired and secret key matches

### Logs
Enable detailed logging by setting:
```yaml
logging:
  level:
    spring.security: DEBUG
    org.springframework.web: DEBUG
```
