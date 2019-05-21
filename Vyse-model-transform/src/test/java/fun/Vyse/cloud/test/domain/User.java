package fun.Vyse.cloud.test.domain;


import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class User {
    private  String name;

    private Date date;

    private List<Address> addresses;
}
