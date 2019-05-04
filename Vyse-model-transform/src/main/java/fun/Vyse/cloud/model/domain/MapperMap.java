package fun.Vyse.cloud.model.domain;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class MapperMap {
    private Configuration configuration;

    private List<Mapper> mappers;
}
