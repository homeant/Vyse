package fun.vyse.cloud.test.domain;


import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class User {
    private  String name;

    private Date date;

    private UserInfo userInfo;

    private List<Address> addressList;
}
