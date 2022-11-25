package vn.ngong.kiotviet.obj;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
public class OrderCustomer {
    private long id;
    private String code;
    private String name;
    private boolean gender;
    private Timestamp birthDate;
    private String contactNumber;
    private String address;
    private String wardName;
    private String email;
    private String comments;
}
