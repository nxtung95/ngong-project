package vn.ngong.kiotviet.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import vn.ngong.kiotviet.obj.*;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@Builder(toBuilder = true)
public class CreateOrdersRequest {
    private boolean isApplyVoucher;
    private Timestamp purchaseDate;
    private int branchId;
    private int soldById;
    private int cashierId;
    private double discount;
    private String description;
    private String method;
    private double totalPayment;
    private int accountId;
    private boolean makeInvoice;
    private Integer saleChannelId;
    private List<OrderDetail> orderDetails;
    private OrderDelivery orderDelivery;
    private OrderCustomer customer;
    private List<OrderSurchages> surchages;
    private List<Object> payments;
}
