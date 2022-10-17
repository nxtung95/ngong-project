package vn.ngong.kiotviet.obj;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class Inventory {
	private int productId;
	private int branchId;
	private String branchName;
	private BigDecimal cost;
	private int onHand;
	private int reserved;
	private int actualReserved;
	private int minQuantity;
	private int maxQuantity;
	private boolean isActive;
}
