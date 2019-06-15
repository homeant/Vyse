package fun.vyse.cloud.design.entity;

import fun.vyse.cloud.core.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;


@Entity
@Table(name = "v_attribute")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@EqualsAndHashCode(callSuper = false)
public class BaseAttribute extends BaseEntity<Long> {

    private String name;

    private String type;

    private String code;
    @Column(name = "`desc`")
    private String desc;
}
