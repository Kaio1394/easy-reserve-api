package br.com.easyreserve.features.module.entity;

import br.com.easyreserve.common.entity.BaseEntity;
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
@Table(name = "tb_modules")
@Getter
@Setter
public class ModuleEntity extends BaseEntity {
    @Column(nullable = false)
    private String slug;

    @Column(name = "name")
    private String name;

    @Column(nullable = false)
    private Boolean active = true;

    @OneToMany(mappedBy = "module")
    private List<WorkspaceModuleEntity> workspaceModules = new ArrayList<>();
}
