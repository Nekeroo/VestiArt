CREATE TABLE idea(
    id BIGINT auto_increment PRIMARY KEY,
    title VARCHAR(256),
    description VARCHAR(1000),
    idExterne CHAR(36),
    image VARCHAR(256),
    pdf VARCHAR(256),
    tag1 VARCHAR(40),
    tag2 VARCHAR(40),
    type VARCHAR(20),
    date_create TIMESTAMP
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
