package br.com.easyreserve.features.environment_resources.entity;

import br.com.easyreserve.common.entity.BaseEntity;
import br.com.easyreserve.features.environment.entity.EnvironmentEntity;
import br.com.easyreserve.features.workspace.entity.WorkspaceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "tb_environment_resources")
public class EnvironmentResourceEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "workspace_id", nullable = false)
    private WorkspaceEntity workspace;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "environment_id")
    private EnvironmentEntity environment;

    @Column(nullable = false)
    private String name;

    @Column
    private Integer quantity;

    @Column(nullable = false)
    private Double weight = 1.0;

    @Column(nullable = false)
    private Boolean operational = true;

    @Column(nullable = false)
    private Boolean active = true;
}
