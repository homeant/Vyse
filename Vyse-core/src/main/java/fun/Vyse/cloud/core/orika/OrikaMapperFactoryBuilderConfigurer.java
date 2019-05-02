package fun.Vyse.cloud.core.orika;

import ma.glasnost.orika.impl.DefaultMapperFactory;

public interface OrikaMapperFactoryBuilderConfigurer {
    /**
     * Configures the {@link DefaultMapperFactory.MapperFactoryBuilder}.
     *
     * @param orikaMapperFactoryBuilder
     *            the {@link DefaultMapperFactory.MapperFactoryBuilder}.
     */
    void configure(DefaultMapperFactory.MapperFactoryBuilder<?, ?> orikaMapperFactoryBuilder);

}
