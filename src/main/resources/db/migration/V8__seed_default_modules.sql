ALTER TABLE tb_modules ADD COLUMN IF NOT EXISTS name VARCHAR(255);

INSERT INTO tb_modules (slug, name, active)
VALUES
    ('MOD-01', 'Painel de Consulta', TRUE),
    ('MOD-02', 'Ambientes & Espaços', TRUE),
    ('MOD-03', 'Elementos & Recursos', TRUE),
    ('MOD-04', 'Controle de Chaves', TRUE),
    ('MOD-05', 'Servidores & Usuários', TRUE)
ON CONFLICT (slug) DO UPDATE
SET name = EXCLUDED.name,
    active = TRUE,
    updated_at = NOW();

-- Associa os módulos padrão aos workspaces já existentes que ainda não possuem módulos vinculados
INSERT INTO tb_workspace_modules (workspace_id, module_id, active)
SELECT w.id, m.id, TRUE
FROM tb_workspaces w
CROSS JOIN tb_modules m
WHERE NOT EXISTS (
    SELECT 1
    FROM tb_workspace_modules wm
    WHERE wm.workspace_id = w.id
)
ON CONFLICT (workspace_id, module_id) DO NOTHING;
