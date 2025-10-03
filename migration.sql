# Database Migration Script for Multi-Tenant Support and Data Type Fixes
# Run this against your PostgreSQL database to add username columns and fix data types

-- Add username column to contacts table if it doesn't exist
ALTER TABLE contacts ADD COLUMN IF NOT EXISTS username VARCHAR(255) NOT NULL DEFAULT 'admin';

-- Fix contacts table id column type with explicit conversion
ALTER TABLE contacts ALTER COLUMN id TYPE BIGINT USING id::bigint;

-- Add username column to groups table if it doesn't exist
ALTER TABLE groups ADD COLUMN IF NOT EXISTS username VARCHAR(255) NOT NULL DEFAULT 'admin';

-- Fix groups table id column type with explicit conversion
ALTER TABLE groups ALTER COLUMN id TYPE BIGINT USING id::bigint;

-- Add username column to interactions table if it doesn't exist
ALTER TABLE interactions ADD COLUMN IF NOT EXISTS username VARCHAR(255) NOT NULL DEFAULT 'admin';

-- Fix interactions table id column type with explicit conversion
ALTER TABLE interactions ALTER COLUMN id TYPE BIGINT USING id::bigint;

-- Create users table for authentication if it doesn't exist
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    enabled BOOLEAN DEFAULT TRUE,
    account_non_expired BOOLEAN DEFAULT TRUE,
    account_non_locked BOOLEAN DEFAULT TRUE,
    credentials_non_expired BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Update interactions notes column to support longer text
ALTER TABLE interactions ALTER COLUMN notes TYPE TEXT;

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_contacts_username ON contacts(username);
CREATE INDEX IF NOT EXISTS idx_groups_username ON groups(username);
CREATE INDEX IF NOT EXISTS idx_interactions_username ON interactions(username);
CREATE INDEX IF NOT EXISTS idx_users_username ON users(username);
