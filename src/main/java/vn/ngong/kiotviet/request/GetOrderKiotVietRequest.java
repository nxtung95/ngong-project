package vn.ngong.kiotviet.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
public class GetOrderKiotVietRequest {
	private Integer[] branchIds;

	private Long[] customerIds;

	private String customerCode;

	private Integer[] status;

	private boolean includePayment;

	private boolean includeOrderDelivery;

	private int pageSize;

	private Integer currentItem;

	private Date lastModifiedFrom;

	private Date toDate;

	private String orderBy;

	private String orderDirection;

	private Date createdDate;
}
