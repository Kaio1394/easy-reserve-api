package br.com.easyreserve.features.workspace_module.entity;

import br.com.easyreserve.common.entity.BaseEntity;
import br.com.easyreserve.features.module.entity.ModuleEntity;
import br.com.easyreserve.features.workspace.entity.WorkspaceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "tb_workspace_modules")
public class WorkspaceModuleEntity extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "workspace_id", nullable = false)
    private WorkspaceEntity workspace;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "module_id", nullable = false)
    private ModuleEntity module;

    @Column(nullable = false)
    private Boolean active = true;
}
