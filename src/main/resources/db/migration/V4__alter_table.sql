ALTER TABLE idea RENAME COLUMN idExterne TO idExterneImage;
ALTER TABLE idea ADD COLUMN idExternePdf CHAR(36);