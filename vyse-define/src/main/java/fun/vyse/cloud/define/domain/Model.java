package fun.vyse.cloud.define.domain;

import com.google.common.collect.Lists;
import fun.vyse.cloud.core.domain.AbstractBaseEntity;
import fun.vyse.cloud.core.domain.IEntity;
import fun.vyse.cloud.core.domain.IModel;
import fun.vyse.cloud.define.entity.specification.SpecConnectionEO;
import fun.vyse.cloud.define.entity.specification.SpecFixedModelEO;
import fun.vyse.cloud.define.entity.specification.SpecModelEO;
import fun.vyse.cloud.define.entity.specification.SpecPropertyEO;
import lombok.Getter;
import lombok.Setter;
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
@ToString(callSuper = true)
public class Model extends AbstractBaseEntity<Long> implements IModel<Long> {

	@Getter
	@Setter
	private SpecModelEO entity;

	@Getter
	@Setter
	private Model parent;

	@Getter
	@Setter
	private String path;

	@Getter
	@Setter
	private SpecFixedModelEO fixedModel;

	private transient MultiKeyMap multiKeyMap = new MultiKeyMap<>();

	public Model(SpecModelEO modelEO) {
		this.entity = modelEO;
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
		if (entity instanceof Model) {
			Model model = (Model) entity;
			String code = model.getCode();
			if (!this.isExist(Model.class, code)) {
				this.put(Model.class, "modelType", entity);
				this.put(Model.class, "code", code, entity);
				this.put(Model.class, "type", model.getType(), entity);
			}
		}

		if (entity instanceof SpecPropertyEO) {
			SpecPropertyEO property = (SpecPropertyEO) entity;
			String code = property.getCode();
			if (!this.isExist(SpecPropertyEO.class, code)) {
				this.put(SpecPropertyEO.class, "modelType", entity);
				this.put(SpecPropertyEO.class, "code", code, entity);
				this.put(SpecPropertyEO.class, "type", "Property", entity);
			}
		}

		if (entity instanceof SpecConnectionEO) {
			SpecConnectionEO connection = (SpecConnectionEO) entity;
			Long parentId = connection.getParentId();
			Long subId = connection.getSubId();
			if (!this.containsKey(SpecConnectionEO.class, parentId, subId)) {
				this.put(SpecConnectionEO.class, "modelType", entity);
				this.put(SpecConnectionEO.class, parentId, subId, entity);
				this.put(SpecConnectionEO.class, "type", connection.getParentType(), connection.getSubType(), entity);
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

	public <T extends IEntity> List<T>  findChildren(Class<T> clazz) {
		return (List)this.multiKeyMap.get(clazz,"type");
	}
}
