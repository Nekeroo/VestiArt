CREATE TABLE bucketinfos (
    id BIGINT auto_increment PRIMARY KEY,
    idExterne CHAR(36),
    url VARCHAR(512),
    tag1 VARCHAR(60),
    tag2 VARCHAR(60),
    tag3 VARCHAR(60),
    date_create TIMESTAMP
);

CREATE TABLE idea(
    id BIGINT auto_increment PRIMARY KEY,
    title VARCHAR(256),
    description VARCHAR(1000),
    tag1 VARCHAR(40),
    tag2 VARCHAR(40),
    image VARCHAR(256)
);

CREATE TABLE request_input (
    id BIGINT NOT NULL AUTO_INCREMENT,
    person VARCHAR(255),
    reference VARCHAR(255),
    type INT,
    idea_id BIGINT,
    PRIMARY KEY (id),
    CONSTRAINT fk_request_input_idea FOREIGN KEY (idea_id) REFERENCES idea(id)
);
