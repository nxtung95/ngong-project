package vn.ngong.kiotviet.obj;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OrderPartnerDelivery {
    private String code;
    private String name;
    private String address;
    private String contactNumber;
    private String email;
}
