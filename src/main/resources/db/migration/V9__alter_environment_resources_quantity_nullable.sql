ALTER TABLE tb_environment_resources
    ALTER COLUMN quantity DROP NOT NULL,
    ALTER COLUMN quantity DROP DEFAULT;
