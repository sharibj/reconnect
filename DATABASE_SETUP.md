# Database Setup for Reconnect Multi-tenant Application

This document describes how to set up the PostgreSQL databases required for the Reconnect application in production mode.

## Architecture Overview

The application uses a discriminator column multi-tenant architecture with:
- **Single Database**: `reconnect` - contains all tenant and domain data
- **Tenant Isolation**: Row-level isolation using `tenant_id` discriminator columns
- **Hibernate Filters**: Automatic filtering ensures tenants only see their own data

## Prerequisites

1. **PostgreSQL Installation**: Ensure PostgreSQL is installed and running
   ```bash
   # macOS with Homebrew
   brew install postgresql
   brew services start postgresql

   # Ubuntu/Debian
   sudo apt-get install postgresql postgresql-contrib
   sudo systemctl start postgresql

   # CentOS/RHEL
   sudo yum install postgresql-server postgresql-contrib
   sudo postgresql-setup initdb
   sudo systemctl start postgresql
   ```

2. **Database Access**: You need superuser access to PostgreSQL to create databases

## Setup Instructions

### 1. Create Required Database

Run the provided setup script as the PostgreSQL superuser:

```bash
# Connect to PostgreSQL as superuser
psql -U postgres

# Run the setup script
\i scripts/setup-databases.sql
```

Alternatively, run the command manually:

```sql
-- Create main application database
CREATE DATABASE reconnect;
```

### 2. Configure Application Properties

The application uses the following environment variables for database connection:

- `DB_USERNAME`: Database username (default: `postgres`)
- `DB_PASSWORD`: Database password (default: `password`)

Set these before running the application:

```bash
# Set environment variables
export DB_USERNAME=your_db_username
export DB_PASSWORD=your_db_password

# Or set them inline when running
DB_USERNAME=your_db_username DB_PASSWORD=your_db_password mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

### 3. Database Schema Management

The application uses Liquibase for database schema management. The schema will be automatically created when the application starts:

- **Single Database**: Contains all tables with tenant isolation via `tenant_id` columns
- **Tenants Table**: Stores tenant authentication and metadata information
- **Domain Tables**: `contacts`, `groups`, and `interactions` tables with `tenant_id` discriminator columns
- **Hibernate Filters**: Automatically filter all queries by the current tenant's ID

#### Liquibase Files Structure

```
src/main/resources/db/changelog/
├── db.changelog-master.xml          # Main changelog file
├── 001-create-tenants-table.xml     # Tenants table schema
└── 002-create-tenant-domain-tables.xml # Domain tables schema
```

## Running the Application

### Development Mode (H2 In-Memory)
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### Production Mode (PostgreSQL)
```bash
# Ensure databases are created first
psql -U postgres -f scripts/setup-databases.sql

# Run the application
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

## Default Demo Account

The setup includes a demo tenant account:
- **Username**: `demo`
- **Password**: `password` (BCrypt hashed)
- **Tenant Name**: `demo_company`
- **Email**: `demo@example.com`

You can log in with these credentials to test the application.

## Adding New Tenants

New tenants can be added by:

1. **Creating the tenant database**:
   ```sql
   CREATE DATABASE tenant_new_company_name;
   ```

2. **Adding tenant record** via the application API or directly in the database:
   ```sql
   INSERT INTO tenants (tenant_name, username, password, email, database_name)
   VALUES ('new_company', 'new_user', '$2a$10$...', 'user@company.com', 'tenant_new_company');
   ```

The application will automatically handle the database routing based on the authenticated user's tenant.

## Troubleshooting

### Common Issues

1. **Database Connection Failed**:
   - Verify PostgreSQL is running
   - Check connection credentials
   - Ensure databases exist

2. **Permission Denied**:
   - Ensure the user has proper database permissions
   - Grant CREATEDB permission if using dynamic tenant creation

3. **Liquibase Errors**:
   - Check if the database is accessible
   - Verify Liquibase changelog files are in the classpath

### Useful Commands

```bash
# Check PostgreSQL status
sudo systemctl status postgresql

# Connect to a specific database
psql -U postgres -d tenants

# List all databases
psql -U postgres -c "\l"

# Drop and recreate databases (development only)
psql -U postgres -c "DROP DATABASE IF EXISTS tenants;"
psql -U postgres -c "CREATE DATABASE tenants;"
```

## Security Considerations

1. **Change Default Passwords**: Update all default passwords in production
2. **Database User Permissions**: Create dedicated application user with minimal required permissions
3. **Network Security**: Configure PostgreSQL to accept connections only from trusted sources
4. **SSL/TLS**: Enable SSL connections for production deployments