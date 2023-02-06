package com.excentria_it.wamya.adapter.persistence.entity;

import com.excentria_it.wamya.common.annotation.Generated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Generated
@Entity
@Table(name = "vehicle_model")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SequenceGenerator(name = ModelJpaEntity.MODEL_SEQ, initialValue = 1, allocationSize = 5)
public class ModelJpaEntity {

    public static final String MODEL_SEQ = "vehicle_model_seq";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = MODEL_SEQ)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "constructor_id")
    private ConstructorJpaEntity constructor;

    private Double length;

    private Double width;

    private Double height;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ConstructorJpaEntity getConstructor() {
        return constructor;
    }

    public void setConstructor(ConstructorJpaEntity constructor) {
        this.constructor = constructor;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
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
        ModelJpaEntity other = (ModelJpaEntity) obj;
        if (id == null) {
            return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "ModelJpaEntity [id=" + id + ", name=" + name + ", constructor=" + constructor + ", length=" + length
                + ", width=" + width + ", height=" + height + "]";
    }


}
