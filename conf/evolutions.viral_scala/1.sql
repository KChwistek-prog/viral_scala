# Users schema

# --- !Ups

CREATE TABLE Users (
    id UUID PRIMARY KEY,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    username VARCHAR(255) UNIQUE NOT NULL,
    hashed_password VARCHAR(255) NOT NULL,
    role VARCHAR(50)
);

# --- !Downs

DROP TABLE Users;