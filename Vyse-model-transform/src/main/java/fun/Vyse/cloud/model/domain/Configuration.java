package fun.vyse.cloud.model.domain;

import lombok.Data;

import java.util.List;

@Data
public class Configuration {

    private List<Converter> customConverters;
}
