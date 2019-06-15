package fun.vyse.cloud.test.domain;

import lombok.Data;

import java.util.List;

@Data
public class UserDto {
    private String nickName;

    private String date;

    private List<Site> adds;
}
