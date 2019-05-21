package fun.Vyse.cloud.orika;

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
