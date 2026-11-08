CREATE TABLE tb_keys (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    environment_id UUID NOT NULL,
    code VARCHAR(100) NOT NULL,
    available BOOLEAN NOT NULL DEFAULT TRUE,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_keys_environment FOREIGN KEY (environment_id) REFERENCES tb_environments(id) ON DELETE RESTRICT,
    CONSTRAINT uq_keys_environment_code UNIQUE (environment_id, code)
);

CREATE INDEX idx_keys_environment_id ON tb_keys(environment_id);

