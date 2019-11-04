/*
 *  Copyright (c) 2011-2014, junchen (junchen1314@foxmail.com).
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not
 *  use this file except in compliance with the License. You may obtain a copy of
 *  the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *  License for the specific language governing permissions and limitations under
 *  the License.
 */

package fun.vyse.cloud.define.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import fun.vyse.cloud.core.common.ApplicationContextHelper;
import fun.vyse.cloud.core.constant.EntityState;
import fun.vyse.cloud.core.constant.SetPropertyType;
import fun.vyse.cloud.core.domain.*;
import fun.vyse.cloud.define.constant.ReservedProperty;
import fun.vyse.cloud.define.entity.actual.ActModelEO;
import fun.vyse.cloud.define.entity.actual.ActPropertyEO;
import fun.vyse.cloud.define.entity.specification.SpecConnectionEO;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.beans.BeanMap;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;


/**
 * fun.vyse.cloud.core.domain.DefineModel
 *
 * @author junchen homeanter@163.com
 * @date 2019-10-12 14:34
 */
@Slf4j
public class DomainModel extends AbstractStateEntity implements IModel<Long> {

	private ActModelEO entity;
	@JsonIgnore
	private transient MetaDefinition<Long> md = null;

	/**
	 * 静态类EO
	 */
	private IFixedEntity<Long> fixedModel = null;

	/**
	 * 静态属性映射
	 */
	private Map<String, String> fixedPropertyMap = null;

	/**
	 * 静态类的数据存储
	 */
	@JsonIgnore
	private transient BeanMap fixedModelMap = null;

	/**
	 * 子级model
	 */
	private Map<String, Object> modelMap = Maps.newConcurrentMap();

	private Map<Long, ActPropertyEO> propertyMap = Maps.newConcurrentMap();

	/**
	 * 调整过的map状态映射
	 */
	private Map<String, EntityState> propertyStateMap = Maps.newConcurrentMap();

	@JsonIgnore
	private List<Long> deleteModelIds = Lists.newArrayList();


	@Getter
	@Setter
	private String tenantId;

	@Getter
	@Setter
	@JsonIgnore
	private DomainModel parentModel;

	public DomainModel() {
		this.entity = null;
	}

	public DomainModel(ActModelEO entity, MetaDefinition md) {
		this.entity = entity;
		this.md = md;
	}

	public void setFixedModel(IFixedEntity fixedModel) {
		this.fixedModel = fixedModel;
		this.fixedModelMap = BeanMap.create(fixedModel);
	}

	private void setFixedModelMap() {
		if (this.fixedModelMap == null) {
			if (this.fixedModel != null) {
				this.fixedModelMap = BeanMap.create(this.fixedModel);
			}
		}
	}

	@Override
	public ActModelEO getEntity() {
		return entity;
	}

	/**
	 * 获取顶级的model
	 *
	 * @return domainModel
	 */
	@JsonIgnore
	public DomainModel getTopModel() {
		return this.parentModel == null ? this : this.parentModel.getTopModel();
	}

	/**
	 * 初始化静态属性映射
	 */
	private void initFixedPropertyMap() {
		if (fixedPropertyMap == null) {
			Long fixedId = this.entity.getFixedId();
			if (fixedId != null && fixedId > 0L) {
				this.initMetaDefinition();
				this.fixedPropertyMap = this.md.getFixedProperty(fixedId);
			}
		}
	}

	private void initMetaDefinition() {
		if (md == null) {
			MetaDefinitionFactory factory = (MetaDefinitionFactory) ApplicationContextHelper.getBean(MetaDefinitionFactory.class);
			if (factory != null) {
				md = factory.getMetaDefinition(this.tenantId);
			} else {
				log.error("no bean found {}", MetaDefinitionFactory.class);
			}
		}
	}

	@JsonIgnore
	public Map<String, Object> getProperty() {
		return this.getProperty(EntityState.None);
	}

	@JsonIgnore
	public Map<String, Object> getProperty(EntityState state) {
		Map<String, Object> property = Maps.newHashMap();
		List<ActPropertyEO> children = this.findChildren(ActPropertyEO.class);
		if (CollectionUtils.isNotEmpty(children)) {
			children.forEach(r -> {
				Integer index = r.getCurrentIndex();
				for (Integer i = 1; i <= index; i++) {
					String key = (String) r.get(ActPropertyEO.PropertyType.code, i);
					if (StringUtils.isNotBlank(key)) {
						if (!property.containsKey(key)) {
							property.put(key, r.get(i));
						}
					}
				}
			});
		}
		EntityState dirtyFlag;
		if (isFixed()) {
			if (this.fixedModel != null) {
				dirtyFlag = this.fixedModel.getDirtyFlag();
				if (state == EntityState.None || dirtyFlag == state) {
					this.initFixedPropertyMap();
					if (this.fixedPropertyMap != null) {
						for (String code : fixedPropertyMap.keySet()) {
							property.put(code, this.getFixedValue(code));
						}
					}
				}
			}
		}
		dirtyFlag = this.entity.getDirtyFlag();
		if (state == EntityState.None || dirtyFlag == state) {
			property.putAll(BeanMap.create(this.entity));
		}
		EntityState state$ = this.getState$();
		property.put(this.STATE$, state$);

		return property;
	}

	/**
	 * 属性赋值
	 *
	 * @param code  编码
	 * @param value 属性值
	 */
	public SetPropertyType setPropertyValue(String code, Object value) {
		this.initFixedPropertyMap();
		SetPropertyType result = SetPropertyType.Failure;
		if (ReservedProperty.valueOf(code)==null) {
			boolean exist = false;
			if (this.isFixedProperty(code)) {//是否是静态属性
				exist = true;
				result = this.setFixedValue(code, value);
			} else {
				for (ActPropertyEO property : this.propertyMap.values()) {
					if (property.containsKey(code)) {
						SetPropertyType setPropertyType = property.set(code, value);
						if (setPropertyType == SetPropertyType.Success) {
							this.getEntity().updateDirtyFlag(EntityState.Modify);
							this.updateState$(code, EntityState.Modify);
							this.updateState$(EntityState.Modify);
							property.updateState$(EntityState.Modify);
						}
						exist = true;
						result = setPropertyType;
						break;
					}
				}
			}
			if (!exist) {

			}
			if (SetPropertyType.Success == result) {
				this.updateState$(EntityState.Modify);
			}
		}
		return result;
	}

	/**
	 * 静态属性赋值
	 *
	 * @param code  编码
	 * @param value 属性值
	 */
	public SetPropertyType setFixedValue(String code, Object value) {
		Object oldValue = this.getFixedValue(code);
		if (ObjectUtils.notEqual(oldValue, value)) {
			String alias = this.fixedPropertyMap.get(code);
			this.fixedModelMap.put(alias, value);
			this.getEntity().updateDirtyFlag(EntityState.Modify);
			this.fixedModel.updateDirtyFlag(EntityState.Modify);
			this.updateState$(code, EntityState.Modify);
			this.updateState$(EntityState.Modify);
			return SetPropertyType.Success;
		} else {
			return SetPropertyType.Null;
		}
	}

	/**
	 * 获取静态属性的值
	 *
	 * @param code 编码
	 * @return 属性值
	 */
	private Object getFixedValue(String code) {
		if (this.fixedPropertyMap != null) {
			String alias = this.fixedPropertyMap.get(code);
			return this.fixedModelMap.get(alias);
		}
		return null;
	}

	public void put(IEntity entity) {
		if (entity instanceof ActPropertyEO) {
			this.put((ActPropertyEO) entity);
		} else if (entity instanceof DomainModel) {
			this.put((DomainModel) entity);
		}
	}

	/**
	 * 添加子级模型
	 *
	 * @param model
	 */
	private void put(DomainModel model) {
		Long id = model.getId();
		Long parentId = model.getEntity().getParentId();
		if (ObjectUtils.notEqual(this.entity.getId(), id) && !ObjectUtils.notEqual(this.entity.getId(), parentId)) {
			String code = model.getEntity().getCode();
			if (this.modelMap.containsKey(code)) {
				Object value = this.modelMap.get(code);
				if (value != null) {
					List<DomainModel> rs;
					if (value instanceof Collection) {
						rs = (List) value;
					} else {
						rs = new ArrayList();
						rs.add((DomainModel) value);
						this.modelMap.put(code, rs);
					}
					rs.add(model);
				} else {
					this.modelMap.put(code, model);
				}
			} else {
				this.modelMap.put(code, model);
			}
		} else {
			log.warn("model relations do not correspond: " +
					"childrenModel: {id: {},parentId :{}}," +
					"model: {id: {}}", model.getId(), model.getEntity().getParentId(), this.entity.getId());
		}
	}

	/**
	 * 添加动态字段
	 *
	 * @param propertyEO 属性对象
	 */
	private void put(ActPropertyEO propertyEO) {
		Long id = propertyEO.getId();
		Long parentId = propertyEO.getParentId();
		if (ObjectUtils.notEqual(this.entity.getId(), id) && !ObjectUtils.notEqual(this.entity.getId(), parentId)) {
			this.propertyMap.put(id, propertyEO);
		} else {
			log.warn("property relations do not correspond: " +
					"property: {id: {},parentId: {}}," +
					"model: {id :{}}", propertyEO.getId(), propertyEO.getParentId(), this.entity.getId());
		}
	}

	@Deprecated
	public Object find(String path){
		return null;
	}

	public Map<String, Object> findChildren() {
		return this.modelMap;
	}

	public <T> List<T> findChildren(Class<T> classType) {
		List<T> childrens = Lists.newArrayList();
		if (classType == DomainModel.class) {
			for (Object model : this.modelMap.values()) {
				if (model instanceof DomainModel) {
					childrens.add((T) model);
				} else if (model instanceof List) {
					childrens.addAll((List<T>) model);
				}
			}
		} else if (classType == ActPropertyEO.class) {
			childrens.addAll((List<T>) this.propertyMap.values().stream().collect(Collectors.toList()));
		} else if (classType == IFixedEntity.class) {
			childrens.add((T) this.fixedModel);
		}
		return childrens;
	}

	public <T extends IEntity> T findChildren(Class<T> classType, String code, Integer index) {
		if (classType == DomainModel.class) {
			Object value = this.modelMap.get(code);
			if (value != null) {
				if (value instanceof DomainModel) {
					return (T) value;
				} else if (value instanceof List && index < ((List) value).size()) {
					return (T) ((List) value).get(index);
				}
			}
		} else if (classType == ActPropertyEO.class) {
			for (ActPropertyEO propertyEO : this.propertyMap.values()) {
				boolean flag = propertyEO.containsKey(code);
				if (flag) {
					return (T) propertyEO;
				}
			}
		}
		return null;
	}

	public <T extends IEntity> List<T> findChildren(Class<T> classType, String code) {
		List<T> list;
		if (classType == DomainModel.class) {
			list = Lists.newArrayList();
			Object value = this.modelMap.get(code);
			if (value != null) {
				if (value instanceof DomainModel) {
					list.add((T) value);
				} else if (value instanceof List) {
					list.addAll((List<T>) value);
				}
			}
			return list;
		} else if (classType == ActPropertyEO.class) {
			list = Lists.newArrayList();
			for (ActPropertyEO propertyEO : this.propertyMap.values()) {
				boolean flag = propertyEO.containsKey(code);
				if (flag) {
					list.add((T) propertyEO);
				}
			}
			return list;
		}
		return null;
	}

	@JsonIgnore
	public Map<String, Object> getData() {
		return this.getInternalModel();
	}

	@JsonIgnore
	private Map<String, Object> getInternalModel() {
		this.initMetaDefinition();
		Map<String, Object> data = this.getProperty();
		EntityState state = this.getState$();
		if (data != null && state != null) {
			data.put(STATE$, state);
		}
		List<DomainModel> childrens = this.findChildren(DomainModel.class);
		if (CollectionUtils.isNotEmpty(childrens)) {
			childrens.stream().forEach(r -> {
				List<SpecConnectionEO> connectionEOS = this.md.getConnections(this.entity.getDomainId(), r.entity.getDomainId());
				if (CollectionUtils.isNotEmpty(connectionEOS)) {
					Map<String, Object> childrenInternalModel = r.getInternalModel();
					SpecConnectionEO connectionEO = connectionEOS.get(0);
					String code = r.entity.getCode();
					Long max = connectionEO.getMaxmium();
					if (data.containsKey(code)) {
						Object value = data.get(code);
						if (value != null) {
							if (value instanceof Map) {
								log.error("model must be an array,code: {},value: {}", code, value);
								throw new RuntimeException("model must be an array");
							}
							if (!(value instanceof List)) {
								log.error("model must be an array,code: {},value: {}", code, value);
								throw new RuntimeException("model must be an array");
							}
							((List) value).add(childrenInternalModel);
						} else {
							if (max > 1) {
								List<Map<String, Object>> lists = Lists.newArrayList(childrenInternalModel);
								data.put(code, lists);
							} else if (max == 1) {
								data.put(code, childrenInternalModel);
							}
						}
					} else {
						if (max > 1) {
							List<Map<String, Object>> lists = Lists.newArrayList(childrenInternalModel);
							data.put(code, lists);
						} else if (max == 1) {
							data.put(code, childrenInternalModel);
						}
					}
				}
			});
		}
		this.cleanState$();
		return data;
	}

	private void setState$(IStateEntity stateEntity) {
		stateEntity.setState$(EntityState.None);
	}

	private void cleanState$() {
		this.setState$(this);
		this.propertyMap.values().forEach(this::setState$);
		if (this.fixedModel != null) {
			this.setState$(this.fixedModel);
		}
		this.propertyStateMap.entrySet().forEach(r -> r.setValue(EntityState.None));
		this.deleteModelIds.clear();

	}

	public void setData(@NonNull Map<String, Object> map) {
		Long domainId = (Long) map.get(ReservedProperty.domainId);
		String code = (String) map.get(ReservedProperty.code);
		if (this.entity != null) {
			if (!ObjectUtils.notEqual(this.entity.getDomainId(), domainId)) {
				if (StringUtils.equals(this.entity.getCode(), code)) {
					for (String r : map.keySet()) {
						Object value = map.get(r);
						if (!(value instanceof Map) && !(value instanceof List)) {
							this.setPropertyValue(r, value);
						} else if (value instanceof Map) {
							DomainModel children = this.findChildren(DomainModel.class, r, 0);
							if (children != null) {
								children.setData((Map) value);
							}
						} else if (value instanceof List) {
							((List) value).stream().filter(this::isMap).forEach(x -> this.setData((Map) x));
						}
					}
				}
			}
		}
	}

	public void updateState$(String code, EntityState state) {
		EntityState entityState = this.propertyStateMap.get(code);
		//原来的状态不是新增 或者 目前的状态不是 修改
		if (EntityState.New != entityState || EntityState.Modify != state) {
			this.propertyStateMap.put(code, state);
		}
	}

	private Boolean isFixedProperty(String code) {
		this.initFixedPropertyMap();
		return this.fixedPropertyMap != null ? this.fixedPropertyMap.containsKey(code) : false;
	}

	@JsonIgnore
	private Boolean isFixed() {
		Long fixedId = this.entity.getFixedId();
		return (fixedId != null && fixedId > 0L) ? true : false;
	}

	private Boolean isMap(Object object) {
		return object instanceof Map;
	}

	public Long getId() {
		return this.entity.getId();
	}

	public void setId(Long id) {
		this.entity.setId(id);
	}

	@Override
	public String toString() {
		return this.entity!=null?this.entity.toString():"";
	}
}
