CREATE TABLE tb_environment_resources (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    workspace_id UUID NOT NULL,
    environment_id UUID,
    name VARCHAR(255) NOT NULL,
    quantity INTEGER NOT NULL DEFAULT 1,
    operational BOOLEAN NOT NULL DEFAULT TRUE,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_env_resources_workspace FOREIGN KEY (workspace_id) REFERENCES tb_workspaces(id) ON DELETE RESTRICT,
    CONSTRAINT fk_env_resources_environment FOREIGN KEY (environment_id) REFERENCES tb_environments(id) ON DELETE SET NULL
);

CREATE INDEX idx_env_resources_workspace_id ON tb_environment_resources(workspace_id);
CREATE INDEX idx_env_resources_environment_id ON tb_environment_resources(environment_id);
