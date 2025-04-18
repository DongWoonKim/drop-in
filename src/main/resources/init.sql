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

CREATE TABLE wod (
                     id BIGINT NOT NULL AUTO_INCREMENT,
                     title VARCHAR(200) NOT NULL,
                     program text NOT NULL,
                     box VARCHAR(100) NOT NULL,
                     created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                     PRIMARY KEY (id)
);

CREATE TABLE record (
                        id       BIGINT       NOT NULL AUTO_INCREMENT,
                        user_id  VARCHAR(30)  NOT NULL,
                        content  TEXT          NOT NULL,
                        date     DATE          NOT NULL,
                        box      VARCHAR(100) NOT NULL,
                        PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;