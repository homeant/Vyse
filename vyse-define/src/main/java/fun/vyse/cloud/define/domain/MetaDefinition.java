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
import fun.vyse.cloud.core.domain.InternalFixedEO;
import fun.vyse.cloud.define.entity.specification.SpecConnectionEO;
import fun.vyse.cloud.define.entity.specification.SpecFixedModelEO;
import fun.vyse.cloud.define.entity.specification.SpecModelEO;
import fun.vyse.cloud.define.entity.specification.SpecPropertyEO;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.beans.BeanMap;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * fun.vyse.cloud.core.domain.MetaDefinition
 *
 * @author junchen homeanter@163.com
 * @date 2019-10-12 14:55
 */
@Slf4j
public class MetaDefinition<T> implements Serializable {

	/**
	 * 模型定义
	 */
	private Map<T, SpecModelEO> model = Maps.newConcurrentMap();

	/**
	 * 属性定义
	 */
	private Map<T, SpecPropertyEO> property = Maps.newConcurrentMap();

	/**
	 * 连接关系
	 */
	private Map<T, Map<T, List<SpecConnectionEO>>> connection = Maps.newConcurrentMap();

	/**
	 * 静态表
	 */
	private Map<T, SpecFixedModelEO> fixedModel = Maps.newConcurrentMap();

	@JsonIgnore
	private transient Map<String, List> domainCacheMap;

	/**
	 * 字段表映射
	 */
	@JsonIgnore
	private transient Map<Long, DualHashBidiMap<String, String>> fixedPropertyMap;

	@JsonIgnore
	private transient Map<String, List<SpecConnectionEO>> connectionMap;

	private final static String CONNECTION_KEY = "ParentId:%s:Type:%s";

	private final static String CODE_KEY = "%s:Code:%s";

	private final static String TYPE_KEY = "%s:Type:%s";


	public void init() {
		this.initDomainMap();
		this.initConnectionMap();
		this.initPropertyMap();
	}

	public void addModel(@NonNull SpecModelEO modelEO) {
		this.model.put((T) modelEO.getId(), modelEO);
	}

	private void putDomainMap(@NonNull Object r) {
		String key = null;
		String typeKey = null;
		if (r instanceof SpecModelEO) {
			key = String.format(CODE_KEY, r.getClass().getSimpleName(), ((SpecModelEO) r).getCode());
			typeKey = String.format(TYPE_KEY, r.getClass().getSimpleName(), ((SpecModelEO) r).getType());
		} else if (r instanceof SpecPropertyEO) {
			key = String.format(CODE_KEY, r.getClass().getSimpleName(), ((SpecPropertyEO) r).getCode());
			typeKey = String.format(TYPE_KEY, r.getClass().getSimpleName(), "Property");
		}
		if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(typeKey)) {
			List list;
			if (this.domainCacheMap.containsKey(key)) {
				list = this.domainCacheMap.get(key);
			} else {
				list = Lists.newArrayList();
				this.domainCacheMap.put(key, list);
			}
			list.add(r);
			if (this.domainCacheMap.containsKey(typeKey)) {
				list = this.domainCacheMap.get(typeKey);
			} else {
				list = Lists.newArrayList();
				this.domainCacheMap.put(typeKey, list);
			}
			list.add(r);
		}
	}

	private void initDomainMap() {
		if (this.domainCacheMap == null) {
			this.domainCacheMap = Maps.newConcurrentMap();
			this.model.values().forEach(this::putDomainMap);
			this.property.values().forEach(this::putDomainMap);
		}
	}

	public SpecModelEO getModel(T id) {
		return this.model.get(id);
	}

	public void addConcurrent(@NonNull SpecConnectionEO connectionEO) {
		final T parentId = (T) connectionEO.getParentId();
		final T subId = (T) connectionEO.getSubId();
		Map<T, List<SpecConnectionEO>> children;
		if (connection.containsKey(parentId)) {
			children = connection.get(parentId);
		} else {
			children = Maps.newConcurrentMap();
			connection.put(parentId, children);
		}
		List<SpecConnectionEO> connectionEOS;
		if (children.containsKey(subId)) {
			connectionEOS = children.get(subId);
		} else {
			connectionEOS = Lists.newArrayList();
			children.put(subId, connectionEOS);
		}
		connectionEOS.add(connectionEO);
	}

	private void initConnectionMap() {
		if (this.connectionMap == null) {
			this.connectionMap = Maps.newConcurrentMap();
			connection.values().stream().map(r -> r.values()).forEach(r -> r.forEach(y -> y.forEach(x -> this.initConnection(x))));
		}
	}

	private void initConnection(@NonNull SpecConnectionEO connectionEO) {
		String key = String.format(CONNECTION_KEY, connectionEO.getParentId(), connectionEO.getSubType());
		List<SpecConnectionEO> connectionEOS;
		if (this.connectionMap.containsKey(key)) {
			connectionEOS = this.connectionMap.get(key);
		} else {
			connectionEOS = Lists.newArrayList();
			this.connectionMap.put(key, connectionEOS);
		}
		connectionEOS.add(connectionEO);
	}

	public void addProperty(@NonNull SpecPropertyEO propertyEO) {
		this.property.put((T) propertyEO.getId(), propertyEO);
	}

	private void initPropertyMap() {
		if (fixedPropertyMap == null) {
			fixedPropertyMap = Maps.newConcurrentMap();
			//内部属性
			BeanMap basePropMap = BeanMap.create(new InternalFixedEO());
			Map<Long, BeanMap> fixedBeanMap = Maps.newHashMap();
			fixedModel.values().stream().forEach(r -> {
				Object instance = r.createFixedInstance();
				if (instance != null) {
					fixedBeanMap.put(r.getId(), BeanMap.create(instance));
				}
			});
			//模型下面的静态表
			this.model.values().stream()
					.filter(r -> r.getFixedId() != null && r.getFixedId() > 0L)
					.forEach(r -> {
						BeanMap beanMap = fixedBeanMap.get(r.getId());
						if (beanMap != null) {
							//SpecFixedModelEO bean = (SpecFixedModelEO) beanMap.getBean();
							List<SpecPropertyEO> childrenProperty = this.findChildrenProperty((T) r.getId());
							if (CollectionUtils.isNotEmpty(childrenProperty)) {
								childrenProperty.stream().forEach(y -> {
									String code = y.getCode();
									String alias = y.getAlias();
									if (StringUtils.isNotBlank(code)) {
										if (StringUtils.isBlank(alias)) {
											alias = code.substring(0, 1).toLowerCase() + code.substring(1);
										}
										if (beanMap != null && basePropMap != null) {
											Long id = r.getFixedId();
											DualHashBidiMap<String, String> dualHashBidiMap = this.fixedPropertyMap.get(id);
											if (dualHashBidiMap == null) {
												dualHashBidiMap = new DualHashBidiMap();
												this.fixedPropertyMap.put(id, dualHashBidiMap);
											}
											dualHashBidiMap.put(code, alias);
										}
									}
								});
							}
						}
					});
		}
	}

	public SpecPropertyEO getProperty(SpecConnectionEO r) {
		return this.getProperty((T) r.getSubId());
	}

	public SpecPropertyEO getProperty(T id) {
		return this.property.get(id);
	}

	private List<SpecPropertyEO> findChildrenProperty(T id) {
		SpecModelEO parent = this.getModel(id);
		if (parent != null) {
			List<SpecConnectionEO> connectionEOS = this.findChildrenConnection(id, "Property");
			if (CollectionUtils.isNotEmpty(connectionEOS)) {
				List<SpecPropertyEO> propertyEOS = connectionEOS.stream().map(this::getProperty).collect(Collectors.toList());
				return propertyEOS;
			}
		}
		return null;
	}

	private List<SpecConnectionEO> findChildrenConnection(T id, String type) {
		String key = String.format(CONNECTION_KEY, id, type);
		return this.connectionMap.get(key);
	}

	public void addFixedModel(SpecFixedModelEO specFixedModelEO) {
		this.fixedModel.put((T) specFixedModelEO.getId(), specFixedModelEO);
	}

	public SpecFixedModelEO getFixedModelEO(T id) {
		return this.fixedModel.get(id);
	}

	/**
	 * 是否是静态的属性
	 *
	 * @param model      单前模型
	 * @param propertyEO 属性
	 * @return
	 */
	public Boolean isFixed(SpecModelEO model, SpecPropertyEO propertyEO) {
		T fixedId = (T) model.getFixedId();
		if (fixedId != null && this.fixedModel.containsKey(fixedId)) {
			List<SpecConnectionEO> connection = this.getConnections((T) model.getId(), (T) propertyEO.getId());
			if (CollectionUtils.isNotEmpty(connection) && connection.size() > 0) {
				SpecConnectionEO connectionEO = connection.get(0);
				if (connectionEO != null) {
					String alias = this.getFixedPropertyAlias(fixedId, propertyEO.getCode());
					if (StringUtils.isNotBlank(alias)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private String getFixedPropertyAlias(T id, String code) {
		if (this.fixedPropertyMap.containsKey(id)) {
			DualHashBidiMap<String, String> ms = this.fixedPropertyMap.get(id);
			return ms.get(code);
		} else {
			return null;
		}
	}

	public List<SpecConnectionEO> getConnections(T id, T subId) {
		Map<T, List<SpecConnectionEO>> connectionMap = this.connection.get(id);
		return Optional.ofNullable(connectionMap).map(r -> r.get(subId)).orElse(null);
	}

	public SpecConnectionEO getConnection(T id, T subId, Date effectiveDate) {
		List<SpecConnectionEO> connections = this.getConnections(id, subId);
		if (CollectionUtils.isNotEmpty(connections)) {
			for (SpecConnectionEO connection : connections) {
				if(isEffectiveConnection(connection,null)){
					return connection;
				}
			}
		}
		return null;
	}

	public Boolean isEffectiveConnection(SpecConnectionEO connectionEO, Date effectiveDate) {
		if (effectiveDate == null) {
			return true;
		}
		return true;
	}

	public Map<String, String> getFixedProperty(T fixedId) {
		this.init();
		return this.fixedPropertyMap.get(fixedId);
	}

	public Model buildModel(Model parent, T id) {
		SpecModelEO modelEO = this.getModel(id);
		if (modelEO != null) {
			Model model = new Model(modelEO);
			if (parent != null) {
				model.setParent(parent);
				String pPath = parent.getPath();
				if (StringUtils.isNotBlank(pPath)) {
					model.setPath(pPath + "." + modelEO.getCode());
				} else {
					model.setPath(modelEO.getCode());
				}
			} else {
				model.setPath(null);
			}
			T fixedId = (T) modelEO.getFixedId();
			if (fixedId != null) {
				SpecFixedModelEO fixedSpecModelEO = this.getFixedModelEO(fixedId);
				model.setFixedModel(fixedSpecModelEO);
			}
			List<SpecConnectionEO> connections = this.findChildrenConnection(id, "Model");
			if (CollectionUtils.isNotEmpty(connections)) {
				connections.forEach(r -> {
					T subId = (T) r.getSubId();
					SpecModelEO subModel = this.getModel(subId);
					if (subModel != null) {
						model.put(r);
						Model childrenModel = this.buildModel(model, subId);
						if (childrenModel != null) {
							model.put(childrenModel);
						}
					}
				});
			}
			connections = this.findChildrenConnection(id, "Property");
			if (CollectionUtils.isNotEmpty(connections)) {
				connections.forEach(r -> {
					T subId = (T) r.getSubId();
					SpecPropertyEO property = this.getProperty(subId);
					if (property != null) {
						model.put(r);
						model.put(property);
					}
				});
			}

			//action
			return model;
		}
		return null;
	}
}
