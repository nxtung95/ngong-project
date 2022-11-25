package vn.ngong.kiotviet.obj;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
public class OrderDelivery {
    private String deliveryCode;
    private int type;
    private double price;
    private String receiver;
    private String contactNumber;
    private String address;
    private int locationId;
    private String locationName;
    private String wardName;
    private double weight;
    private double length;
    private double width;
    private double height;
    private long partnerDeliveryId;
    private Timestamp expectedDelivery;
    private OrderPartnerDelivery partnerDelivery;
}
