CREATE TABLE request_input (
                               id BIGINT NOT NULL AUTO_INCREMENT,
                               person VARCHAR(255),
                               reference VARCHAR(255),
                               type INT,
                               idea_id BIGINT,
                               PRIMARY KEY (id),
                               CONSTRAINT fk_request_input_idea FOREIGN KEY (idea_id) REFERENCES idea(id)
);
