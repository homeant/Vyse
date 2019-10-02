package fun.vyse.cloud.core.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@MappedSuperclass
public class BaseEntity<T> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private T id;

    @Column(updatable = false)
    private Date createTime;

    private Date updateTime;

    @PrePersist
    public void onCreate(){
        this.createTime = new Date();
    }

    @PreUpdate
    public void onUpdate(){
        this.updateTime = new Date();
    }

}
