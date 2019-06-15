package fun.vyse.cloud.design.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "v_attribute_ext")
@PrimaryKeyJoinColumn(name = "attr_id")
@Data
@EqualsAndHashCode(callSuper = false)
public class Attribute extends BaseAttribute{

    private int length;

    private boolean visible;

    private int required;
}
