package br.com.easyreserve.features.key.entity;

import br.com.easyreserve.common.entity.BaseEntity;
import br.com.easyreserve.features.environment.entity.EnvironmentEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "tb_keys")
public class KeyEntity extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "environment_id", nullable = false)
    private EnvironmentEntity environment;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private Boolean available = true;

    @Column(nullable = false)
    private Boolean active = true;
}
