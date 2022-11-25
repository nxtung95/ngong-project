package vn.ngong.kiotviet.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import vn.ngong.kiotviet.obj.OrderDelivery;
import vn.ngong.kiotviet.obj.OrderDetail;
import vn.ngong.kiotviet.obj.OrderInvoiceSurcharges;
import vn.ngong.kiotviet.obj.OrderPayment;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@Builder
public class CreateOrdersResponse {
    private long id;
    private String code;
    private Timestamp purchaseDate;
    private int branchId;
    private String branchName;
    private long soldById;
    private String soldByName;
    private long customerId;
    private String customerName;
    private double total;
    private double totalPayment;
    private double discountRatio;
    private double discount;
    private String method;
    private int status;
    private String statusValue;
    private String description;
    private boolean usingCod;
    private int saleChannelId;
    private List<OrderDetail> orderDetails;
    private OrderDelivery orderDelivery;
    private List<OrderPayment> payments;
    private List<OrderInvoiceSurcharges> invoiceOrderSurcharges;
}
