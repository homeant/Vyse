package fun.vyse.cloud.design.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "v_attribute_meta")
@PrimaryKeyJoinColumn(name = "attr_id")
@Data
@EqualsAndHashCode(callSuper = false)
public class AttributeMeta extends Attribute {

    private int length;

    private boolean visible;

    private int required;

    private int businessType;
}
