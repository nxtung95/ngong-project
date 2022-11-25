package vn.ngong.kiotviet.obj;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class OrderInvoiceSurcharges {
    private long id;
    private long invoiceId;
    private long surchargeId;
    private String surchargeName;
    private double surValue;
    private double price;
    private Timestamp createdDate;
}
