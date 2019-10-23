package fun.vyse.cloud.core.repository;

import java.io.Serializable;

/**
 * ValueObject 值对象
 * <p>如果值对象是单一属性，则直接定义为实体的属性</p>
 * <p>如果值对象是属性集合，则把它设计为Class类，Class将具有整体概念的多个属性归集到属性集合，该值对象没有ID，因为它被值对象引用</p>
 * @author junchen homeanter@163.com
 * @date 2019-10-23 13:32
 */
public interface ValueObject extends Serializable,Cloneable {

}
