package fun.vyse.cloud.design.entity;

import fun.vyse.cloud.core.model.BaseEntity;
import fun.vyse.cloud.design.constant.EntityType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;


@Entity
@Table(name = "v_model")
@Data
@EqualsAndHashCode(callSuper = false)
public class Model extends BaseEntity<Long> {

    private String name;

    private String code;

    private EntityType type = EntityType.COMPONENT;
}
