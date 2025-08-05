CREATE TABLE files
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    data         LONGBLOB     NOT NULL,
    content_type VARCHAR(255) NOT NULL,
    idea_id      BIGINT,
    FOREIGN KEY (idea_id) REFERENCES idea (id)
);

ALTER TABLE idea
    DROP COLUMN image,           -- Ancienne colonne pour l'image, remplacée par une relation avec File
    DROP COLUMN pdf,             -- Ancienne colonne pour le PDF, remplacée par une relation avec File
    DROP COLUMN idExterneImage,  -- Ancienne colonne pour l'ID externe de l'image
    DROP COLUMN idExternePdf;    -- Ancienne colonne pour l'ID externe du PDF


ALTER TABLE idea
    ADD COLUMN image_id BIGINT,  -- Colonne pour stocker l'ID du fichier image
    ADD COLUMN pdf_id BIGINT;    -- Colonne pour stocker l'ID du fichier PDF

-- Ajout des clés étrangères pour lier les fichiers à l'idée
ALTER TABLE idea
    ADD CONSTRAINT FK_image FOREIGN KEY (image_id) REFERENCES files(id),
    ADD CONSTRAINT FK_pdf FOREIGN KEY (pdf_id) REFERENCES files(id);
