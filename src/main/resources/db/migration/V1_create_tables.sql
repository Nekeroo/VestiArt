CREATE TABLE bucketinfos (
    id BIGINT auto_increment PRIMARY KEY,
    idExterne CHAR(36),
    url VARCHAR(512),
    tag1 VARCHAR(60),
    tag2 VARCHAR(60),
    tag3 VARCHAR(60)
);

CREATE TABLE idea(
    id BIGINT auto_increment PRIMARY KEY,
    title VARCHAR(256),
    description VARCHAR(1000)
);