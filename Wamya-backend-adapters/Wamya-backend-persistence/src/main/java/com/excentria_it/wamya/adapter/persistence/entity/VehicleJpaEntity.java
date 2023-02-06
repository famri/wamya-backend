package com.excentria_it.wamya.adapter.persistence.entity;

import com.excentria_it.wamya.common.annotation.Generated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Generated
@Entity
@Table(name = "vehicle")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SequenceGenerator(name = VehicleJpaEntity.VEHICLE_SEQ, initialValue = 1, allocationSize = 5)
public class VehicleJpaEntity {

    public static final String VEHICLE_SEQ = "vehicle_seq";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = VEHICLE_SEQ)
    private Long id;

    @ManyToOne
    private EngineTypeJpaEntity type;

    @ManyToOne
    private ModelJpaEntity model;

    private LocalDate circulationDate;

    private String registration;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "temporary_model_id", unique = true, nullable = true, updatable = true)
    private TemporaryModelJpaEntity temporaryModel;

    @ManyToOne
    protected DocumentJpaEntity image;

    public Long getId() {
        return id;
    }

    public EngineTypeJpaEntity getType() {
        return type;
    }

    public void setType(EngineTypeJpaEntity type) {
        this.type = type;
    }

    public ModelJpaEntity getModel() {
        return model;
    }

    public void setModel(ModelJpaEntity model) {
        this.model = model;
    }

    public LocalDate getCirculationDate() {
        return circulationDate;
    }

    public void setCirculationDate(LocalDate circulationDate) {
        this.circulationDate = circulationDate;
    }

    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public TemporaryModelJpaEntity getTemporaryModel() {
        return temporaryModel;
    }

    public void setTemporaryModel(TemporaryModelJpaEntity temporaryModel) {
        this.temporaryModel = temporaryModel;
    }

    public DocumentJpaEntity getImage() {
        return image;
    }

    public void setImage(DocumentJpaEntity image) {
        this.image = image;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        VehicleJpaEntity other = (VehicleJpaEntity) obj;
        if (id == null) {
            return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "VehicleJpaEntity [id=" + id + ", type=" + type + ", model=" + model + ", circulationDate="
                + circulationDate + ", registration=" + registration + "]";
    }

}
