CREATE TABLE tb_reservation_histories (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    workspace_id UUID NOT NULL,
    reservation_id UUID,
    environment_id UUID NOT NULL,
    key_id UUID NOT NULL,
    user_id UUID NOT NULL,
    action VARCHAR(40) NOT NULL,
    purpose VARCHAR(255),
    start_time TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    end_time TIMESTAMP WITHOUT TIME ZONE,
    status VARCHAR(30) NOT NULL,
    notes VARCHAR(255),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_reservation_histories_workspace FOREIGN KEY (workspace_id) REFERENCES tb_workspaces(id) ON DELETE RESTRICT,
    CONSTRAINT fk_reservation_histories_reservation FOREIGN KEY (reservation_id) REFERENCES tb_reservations(id) ON DELETE SET NULL,
    CONSTRAINT fk_reservation_histories_environment FOREIGN KEY (environment_id) REFERENCES tb_environments(id) ON DELETE RESTRICT,
    CONSTRAINT fk_reservation_histories_key FOREIGN KEY (key_id) REFERENCES tb_keys(id) ON DELETE RESTRICT,
    CONSTRAINT fk_reservation_histories_user FOREIGN KEY (user_id) REFERENCES tb_users(id) ON DELETE RESTRICT
);

CREATE INDEX idx_reservation_histories_workspace_id ON tb_reservation_histories(workspace_id);
CREATE INDEX idx_reservation_histories_reservation_id ON tb_reservation_histories(reservation_id);
CREATE INDEX idx_reservation_histories_environment_id ON tb_reservation_histories(environment_id);
CREATE INDEX idx_reservation_histories_key_id ON tb_reservation_histories(key_id);
CREATE INDEX idx_reservation_histories_user_id ON tb_reservation_histories(user_id);

-- Gera registros iniciais de histórico para as reservas já existentes
INSERT INTO tb_reservation_histories (
    workspace_id,
    reservation_id,
    environment_id,
    key_id,
    user_id,
    action,
    purpose,
    start_time,
    end_time,
    status,
    notes,
    active,
    created_at,
    updated_at
)
SELECT
    r.workspace_id,
    r.id,
    r.environment_id,
    r.key_id,
    r.user_id,
    CASE
        WHEN r.status = 'COMPLETED' THEN 'COMPLETED'
        WHEN r.status = 'CANCELLED' THEN 'CANCELLED'
        ELSE 'CREATED'
    END,
    r.purpose,
    r.start_time,
    r.end_time,
    r.status,
    CASE
        WHEN r.status = 'COMPLETED' THEN 'Reserva encerrada e chave devolvida à portaria'
        WHEN r.status = 'CANCELLED' THEN 'Reserva cancelada e chave liberada'
        ELSE 'Reserva confirmada — chave retirada (EM USO)'
    END,
    r.active,
    r.created_at,
    r.updated_at
FROM tb_reservations r;
