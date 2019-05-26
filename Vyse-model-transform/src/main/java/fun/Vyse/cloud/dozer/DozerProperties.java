package fun.Vyse.cloud.dozer;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;


@Data
@ConfigurationProperties("dozer")
public class DozerProperties {
    private static final ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();

    private Boolean enabled;

    private String[] mappingFiles;

    public Resource[] resolveMappingLocations() {
        return Stream.of(Optional.ofNullable(this.mappingFiles).orElse(new String[0]))
                .flatMap(location -> Stream.of(getResources(location)))
                .toArray(Resource[]::new);
    }

    private Resource[] getResources(String location) {
        try {
            return resourceResolver.getResources(location);
        } catch (IOException e) {
            return new Resource[0];
        }
    }
}
