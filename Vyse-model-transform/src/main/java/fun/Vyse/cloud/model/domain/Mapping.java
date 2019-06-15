package fun.vyse.cloud.model.domain;

import lombok.Data;

import java.util.List;

@Data
public class Mapping {
    private String classA;

    private String classB;

    private Boolean mapNull;

    private String customConverter;

    private List<Field> fields;
}
