CREATE TABLE tb_workspace_modules (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    workspace_id UUID NOT NULL,
    module_id UUID NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_workspace_modules_workspace FOREIGN KEY (workspace_id) REFERENCES tb_workspaces(id) ON DELETE RESTRICT,
    CONSTRAINT fk_workspace_modules_module FOREIGN KEY (module_id) REFERENCES tb_modules(id) ON DELETE RESTRICT,
    CONSTRAINT uq_workspace_modules_workspace_module UNIQUE (workspace_id, module_id)
);

CREATE INDEX idx_workspace_modules_workspace_id ON tb_workspace_modules(workspace_id);
CREATE INDEX idx_workspace_modules_module_id ON tb_workspace_modules(module_id);
