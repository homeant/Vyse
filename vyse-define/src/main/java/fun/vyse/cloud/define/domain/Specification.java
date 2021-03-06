package fun.vyse.cloud.define.domain;

import com.google.common.collect.Lists;
import fun.vyse.cloud.core.domain.AbstractBaseEntity;
import fun.vyse.cloud.core.domain.IEntity;
import fun.vyse.cloud.core.domain.IModel;
import fun.vyse.cloud.define.entity.specification.ConnectionSpecEO;
import fun.vyse.cloud.define.entity.specification.FixedModelSpecEO;
import fun.vyse.cloud.define.entity.specification.ModelSpecEO;
import fun.vyse.cloud.define.entity.specification.PropertySpecEO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.keyvalue.MultiKey;
import org.apache.commons.collections4.map.MultiKeyMap;

import java.util.List;

/**
 * Model
 *
 * @author junchen homeanter@163.com
 * @date 2019-10-28 14:23
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Specification extends AbstractBaseEntity<Long> implements IModel<Long> {

	private ModelSpecEO entity;

	private Specification parent;

	private String path;

	private FixedModelSpecEO fixedModel;

	private final transient MultiKeyMap multiKeyMap = new MultiKeyMap<>();

	public Specification(ModelSpecEO modelEO) {
		this.entity = modelEO;
	}

	@Override
	public Long getId() {
		return this.entity.getId();
	}

	public String getCode() {
		return this.entity.getCode();
	}

	public String getType() {
		return this.entity.getType();
	}

	public void put(IEntity entity) {
		MultiKey key = new MultiKey(new Object[]{entity.getId()});
		this.multiKeyMap.put(key, entity);
		if (entity instanceof Specification) {
			Specification model = (Specification) entity;
			String code = model.getCode();
			if (!this.isExist(Specification.class, code)) {
				this.put(Specification.class, "specType", entity);
				this.put(Specification.class, "code", code, entity);
				this.put(Specification.class, "type", model.getType(), entity);
			}
		}

		if (entity instanceof PropertySpecEO) {
			PropertySpecEO property = (PropertySpecEO) entity;
			String code = property.getCode();
			if (!this.isExist(PropertySpecEO.class, code)) {
				this.put(PropertySpecEO.class, "specType", entity);
				this.put(PropertySpecEO.class, "code", code, entity);
				this.put(PropertySpecEO.class, "type", "Property", entity);
			}
		}

		if (entity instanceof ConnectionSpecEO) {
			ConnectionSpecEO connection = (ConnectionSpecEO) entity;
			Long parentId = connection.getParentId();
			Long subId = connection.getSubId();
			if (!this.containsKey(ConnectionSpecEO.class, parentId, subId)) {
				this.put(ConnectionSpecEO.class, "specType", entity);
				this.put(ConnectionSpecEO.class, parentId, subId, entity);
				this.put(ConnectionSpecEO.class, "type", connection.getParentType(), connection.getSubType(), entity);
			}
		}
		// anction
	}

	private void put(Object key, Object key2, IEntity value) {
		List<IEntity> lists = (List<IEntity>) this.multiKeyMap.get(key, key2);
		if (CollectionUtils.isEmpty(lists)) {
			lists = Lists.newArrayList();
			this.multiKeyMap.put(key, key2, lists);
		}
		lists.add(value);
	}

	private void put(Object key, Object key2, Object key3, IEntity value) {
		List<IEntity> lists = (List<IEntity>) this.multiKeyMap.get(key, key2, key3);
		if (CollectionUtils.isEmpty(lists)) {
			lists = Lists.newArrayList();
			this.multiKeyMap.put(key, key2, key3, lists);
		}
		lists.add(value);
	}

	private void put(Object key, Object key2, Object key3, Object key4, IEntity value) {
		List<IEntity> lists = (List<IEntity>) this.multiKeyMap.get(key, key2, key3, key4);
		if (CollectionUtils.isEmpty(lists)) {
			lists = Lists.newArrayList();
			this.multiKeyMap.put(key, key2, key3, key4, lists);
		}
		lists.add(value);
	}


	private Boolean containsKey(Object key, Object key2, Object key3) {
		return this.multiKeyMap.containsKey(key, key2, key3);
	}

	private Boolean isExist(Class<? extends IEntity> entity, String code) {
		return this.containsKey(entity, "code", code);
	}

	public <T extends IEntity> List<T> findChildren(Class<T> clazz) {
		return (List)this.multiKeyMap.get(clazz,"specType");
	}

	public List<ConnectionSpecEO> getConnection(Long id){
		return (List)this.multiKeyMap.get(ConnectionSpecEO.class, this.getId(), id);
	}
}
