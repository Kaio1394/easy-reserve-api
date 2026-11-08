package br.com.easyreserve.features.environment.entity;

import br.com.easyreserve.common.entity.BaseEntity;
import br.com.easyreserve.features.environment_resources.entity.EnvironmentResourceEntity;
import br.com.easyreserve.features.key.entity.KeyEntity;
import br.com.easyreserve.features.workspace.entity.WorkspaceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_environments")
@Getter
@Setter
public class EnvironmentEntity extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "workspace_id", nullable = false)
    private WorkspaceEntity workspace;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Column
    private Integer capacity;

    @Column(nullable = false)
    private Boolean active = true;

    @OneToMany(mappedBy = "environment")
    private List<KeyEntity> keys = new ArrayList<>();

    @OneToMany(mappedBy = "environment")
    private List<EnvironmentResourceEntity> resources = new ArrayList<>();
}
