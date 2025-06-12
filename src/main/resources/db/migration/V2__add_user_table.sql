CREATE TABLE role
(
    id   BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE user
(
    id       BIGINT NOT NULL AUTO_INCREMENT,
    username VARCHAR(255),
    password VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT exists user_roles
(
    users_id BIGINT,
    roles_id BIGINT,
    FOREIGN KEY (users_id) REFERENCES user (id),
    FOREIGN KEY (roles_id) REFERENCES role (id),
    PRIMARY KEY (users_id, roles_id)
);
