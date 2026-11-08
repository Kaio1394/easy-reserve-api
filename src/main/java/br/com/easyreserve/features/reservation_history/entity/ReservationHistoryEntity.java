package br.com.easyreserve.features.reservation_history.entity;

import br.com.easyreserve.common.entity.BaseEntity;
import br.com.easyreserve.features.environment.entity.EnvironmentEntity;
import br.com.easyreserve.features.key.entity.KeyEntity;
import br.com.easyreserve.features.reservation.entity.ReservationEntity;
import br.com.easyreserve.features.reservation.enums.ReservationStatus;
import br.com.easyreserve.features.reservation_history.enums.ReservationHistoryAction;
import br.com.easyreserve.features.user.entity.UserEntity;
import br.com.easyreserve.features.workspace.entity.WorkspaceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "tb_reservation_histories")
public class ReservationHistoryEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "workspace_id", nullable = false)
    private WorkspaceEntity workspace;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private ReservationEntity reservation;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "environment_id", nullable = false)
    private EnvironmentEntity environment;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "key_id", nullable = false)
    private KeyEntity key;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    @Column(name = "action", nullable = false)
    private ReservationHistoryAction action;

    @Column(name = "purpose")
    private String purpose;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ReservationStatus status;

    @Column(name = "notes")
    private String notes;

    @Column(nullable = false)
    private Boolean active = true;
}
