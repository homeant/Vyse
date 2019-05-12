package fun.Vyse.cloud.model.domain;

import lombok.Data;

import java.util.List;

@Data
public class Mapping {
    private String classA;

    private String classB;

    private List<Field> fields;
}
