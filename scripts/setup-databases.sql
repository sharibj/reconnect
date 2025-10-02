-- PostgreSQL Database Setup Script for Reconnect Multi-tenant Application
-- Run this script as a PostgreSQL superuser (e.g., postgres user)

-- Create main application database (contains both tenant and domain data)
CREATE DATABASE reconnect;

-- Create a user for the application (optional, you can use postgres user)
-- CREATE USER reconnect_app WITH PASSWORD 'your_secure_password';
-- GRANT ALL PRIVILEGES ON DATABASE reconnect TO reconnect_app;

\echo 'Database setup completed successfully!'
\echo 'The application uses a single database with tenant_id discriminator columns for multi-tenancy.'
\echo 'You can now run the application with:'
\echo 'mvn spring-boot:run -Dspring-boot.run.profiles=prod'