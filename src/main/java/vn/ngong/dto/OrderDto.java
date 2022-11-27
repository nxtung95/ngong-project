package vn.ngong.dto;


import lombok.*;
import vn.ngong.kiotviet.obj.OrderDetail;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDto {
	private String orderCode;
	private String purchaseDate;
	private int status;
	private String statusValue;
	private String description;
	private double totalAmount;
	private String createdDate;
	private String modifiedDate;
	private List<OrderDetail> orderDetails;
}
