CREATE DATABASE crossfit
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

CREATE TABLE member (
                        id BIGINT NOT NULL AUTO_INCREMENT,
                        user_id VARCHAR(30) NOT NULL UNIQUE,
                        password VARCHAR(200) NOT NULL,
                        user_name VARCHAR(20) NOT NULL,
                        role VARCHAR(20) NOT NULL DEFAULT 'ROLE_USER',
                        status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
                        phone VARCHAR(15) NOT NULL,
                        box VARCHAR(100) NOT NULL,
                        birth DATE NOT NULL,
                        email VARCHAR(50) NOT NULL,
                        created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        PRIMARY KEY (id)
);