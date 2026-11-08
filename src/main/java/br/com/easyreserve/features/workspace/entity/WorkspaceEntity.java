package br.com.easyreserve.features.workspace.entity;

import br.com.easyreserve.common.entity.BaseEntity;
import br.com.easyreserve.features.environment.entity.EnvironmentEntity;
import br.com.easyreserve.features.environment_resources.entity.EnvironmentResourceEntity;
import br.com.easyreserve.features.user.entity.UserEntity;
import br.com.easyreserve.features.workspace_module.entity.WorkspaceModuleEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "tb_workspaces")
public class WorkspaceEntity extends BaseEntity {
    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String slug;

    @Column(nullable = false)
    private Boolean active = true;

    @OneToMany(mappedBy = "workspace")
    private List<UserEntity> users = new ArrayList<>();

    @OneToMany(mappedBy = "workspace")
    private List<EnvironmentEntity> environments = new ArrayList<>();

    @OneToMany(mappedBy = "workspace")
    private List<EnvironmentResourceEntity> environmentResources = new ArrayList<>();

    @OneToMany(mappedBy = "workspace")
    private List<WorkspaceModuleEntity> workspaceModules = new ArrayList<>();
}
