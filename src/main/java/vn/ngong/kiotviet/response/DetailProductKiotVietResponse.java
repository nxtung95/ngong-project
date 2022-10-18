package vn.ngong.kiotviet.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import vn.ngong.kiotviet.obj.Attribute;
import vn.ngong.kiotviet.obj.Inventory;
import vn.ngong.kiotviet.obj.ResponseStatus;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder(toBuilder = true)
public class DetailProductKiotVietResponse {
	private String createdDate;
	private String masterCode;
	private int id;
	private int retailerId;
	private String code;
	private String barCode;
	private String name;
	private String fullName;
	private int categoryId;
	private String categoryName;
	private String allowsSale;
	private int type;
	private boolean hasVariants;
	private BigDecimal basePrice;
	private int weight;
	private String unit;
	private String conversionValue;
	private String description;
	private boolean isActive;
	private boolean isRewardPoint;
	private String orderTemplate;
	private boolean isLotSerialControl;
	private List<Attribute> attributes;
	private List<Inventory> inventories;

	private ResponseStatus responseStatus;
}
