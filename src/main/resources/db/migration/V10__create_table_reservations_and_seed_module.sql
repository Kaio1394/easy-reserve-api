CREATE TABLE tb_reservations (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    workspace_id UUID NOT NULL,
    environment_id UUID NOT NULL,
    key_id UUID NOT NULL,
    user_id UUID NOT NULL,
    purpose VARCHAR(255),
    start_time TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    end_time TIMESTAMP WITHOUT TIME ZONE,
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_reservations_workspace FOREIGN KEY (workspace_id) REFERENCES tb_workspaces(id) ON DELETE RESTRICT,
    CONSTRAINT fk_reservations_environment FOREIGN KEY (environment_id) REFERENCES tb_environments(id) ON DELETE RESTRICT,
    CONSTRAINT fk_reservations_key FOREIGN KEY (key_id) REFERENCES tb_keys(id) ON DELETE RESTRICT,
    CONSTRAINT fk_reservations_user FOREIGN KEY (user_id) REFERENCES tb_users(id) ON DELETE RESTRICT
);

CREATE INDEX idx_reservations_workspace_id ON tb_reservations(workspace_id);
CREATE INDEX idx_reservations_environment_id ON tb_reservations(environment_id);
CREATE INDEX idx_reservations_key_id ON tb_reservations(key_id);
CREATE INDEX idx_reservations_user_id ON tb_reservations(user_id);

-- Adiciona o módulo padrão de Reserva de Ambientes (MOD-06)
INSERT INTO tb_modules (slug, name, active)
VALUES ('MOD-06', 'Reserva de Ambientes', TRUE)
ON CONFLICT (slug) DO UPDATE
SET name = EXCLUDED.name,
    active = TRUE,
    updated_at = NOW();

-- Vincula automaticamente o novo módulo padrão aos workspaces já existentes
INSERT INTO tb_workspace_modules (workspace_id, module_id, active)
SELECT w.id, m.id, TRUE
FROM tb_workspaces w
CROSS JOIN tb_modules m
WHERE m.slug = 'MOD-06'
ON CONFLICT (workspace_id, module_id) DO NOTHING;
