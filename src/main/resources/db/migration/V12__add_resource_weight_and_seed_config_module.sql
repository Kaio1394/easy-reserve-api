-- Adiciona a coluna de peso (weight) para os recursos/elementos (padrão 1.0)
ALTER TABLE tb_environment_resources
ADD COLUMN IF NOT EXISTS weight DOUBLE PRECISION NOT NULL DEFAULT 1.0;

-- Adiciona o módulo padrão de Configurações (MOD-07)
INSERT INTO tb_modules (slug, name, active)
VALUES ('MOD-07', 'Configurações', TRUE)
ON CONFLICT (slug) DO UPDATE
SET name = EXCLUDED.name,
    active = TRUE,
    updated_at = NOW();

-- Vincula automaticamente o novo módulo padrão aos workspaces já existentes
INSERT INTO tb_workspace_modules (workspace_id, module_id, active)
SELECT w.id, m.id, TRUE
FROM tb_workspaces w
CROSS JOIN tb_modules m
WHERE m.slug = 'MOD-07'
ON CONFLICT (workspace_id, module_id) DO NOTHING;
