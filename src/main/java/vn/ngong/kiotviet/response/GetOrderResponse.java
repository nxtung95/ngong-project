package vn.ngong.kiotviet.response;

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
public class GetOrderResponse {
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
    private int status;
    private String statusValue;
    private String description;
    private boolean usingCod;
    private List<OrderPayment> payments;
    private List<OrderDetail> orderDetails;
    private OrderDelivery orderDelivery;
    private OrderInvoiceSurcharges invoiceOrderSurcharges;
    private int retailerId;
    private Timestamp modifiedDate;
    private Timestamp createdDate;
}
