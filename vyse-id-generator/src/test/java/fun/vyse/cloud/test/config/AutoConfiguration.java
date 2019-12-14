package fun.vyse.cloud.test.config;

import fun.vyse.cloud.converter.IdConverter;
import fun.vyse.cloud.converter.impl.IdConverterImpl;
import fun.vyse.cloud.enums.IdType;
import fun.vyse.cloud.provider.MachineIdProvider;
import fun.vyse.cloud.provider.PropertyMachineIdProvider;
import fun.vyse.cloud.service.IdService;
import fun.vyse.cloud.service.impl.IdServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * AutoConfiguration
 *
 * @author junchen
 * @date 2019-12-14 16:32
 */
@Configuration
public class AutoConfiguration {
	@Bean
	public MachineIdProvider machineIdProvider() {
		PropertyMachineIdProvider provider = new PropertyMachineIdProvider();
		provider.setMachineId(1L);
		return provider;
	}

	@Bean
	public IdConverter idConverter() {
		return new IdConverterImpl(IdType.MAX_PEAK);
	}

	@Bean(initMethod = "init")
	public IdService idService(MachineIdProvider provider, IdConverter converter) {
		IdServiceImpl idService = new IdServiceImpl(IdType.MAX_PEAK);
		idService.setMachineIdProvider(provider);
		idService.setIdConverter(converter);
		return idService;
	}
}
