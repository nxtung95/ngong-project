package vn.ngong.kiotviet.obj;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
public class OrderPayment {
    private long id;
    private String code;
    private double amount;
    private String method;
    private int status;
    private String statusValue;
    private Timestamp transDate;
    private String bankAccount;
    private int accountId;
}
