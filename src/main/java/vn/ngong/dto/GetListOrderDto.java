package vn.ngong.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class GetListOrderDto {
	private String customerCode;
	private String customerName;
	private int total;
	private int pageSize;
	private List<OrderDto> orders;
}
