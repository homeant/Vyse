package fun.vyse.cloud.define.boot;

import fun.vyse.cloud.define.domain.MetaDefinitionFactory;
import fun.vyse.cloud.define.service.*;
import fun.vyse.cloud.define.service.impl.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * DefineAutoConfiguration
 *
 * @author junchen homeanter@163.com
 * @date 2019-10-24 19:07
 */
@Slf4j
@Configuration
@EntityScan(basePackages = {"fun.vyse.cloud.core.domain","fun.vyse.cloud.define.entity"})
@EnableJpaRepositories(basePackages = {"fun.vyse.cloud.core.repository","fun.vyse.cloud.define.repository"})
public class DefineAutoConfiguration {

	public DefineAutoConfiguration(){
		log.info("DefineAutoConfiguration init ...");
	}

	@Bean
	public IModelSpecService modelService(){
		return new ModelSpecServiceImpl();
	}

	@Bean
	public IPropertySpecService propertyService(){
		return new PropertySpecServiceImpl();
	}

	@Bean
	public IConnectionSpecService connectionService(){
		return new ConnectionSpecServiceImpl();
	}

	@Bean
	public IFixedModelSpecService fixedModelService(){
		return new FixedModelSpecServiceImpl();
	}

	@Bean
	public IMetaDefinitionService metaDefinitionService(){
		return new MetaDefinitionServiceImpl();
	}

	@Bean
	public MetaDefinitionFactory metaDefinitionFactory(IMetaDefinitionService metaDefinitionService){
		return new MetaDefinitionFactory(metaDefinitionService);
	}

	@Bean
	public IModelActService modelDataService(){
		return new ModelActServiceImpl();
	}

	@Bean
	public IPropertyActService modelPropertyService(){
		return new PropertyActServiceImpl();
	}

	@Bean
	public IDomainEntityService domainEntityService(){
		return new DomainEntityServiceImpl();
	}

	@Bean
	public IDomainModelService domainModelService(){
		return new DomainModelServiceImpl();
	}
}
