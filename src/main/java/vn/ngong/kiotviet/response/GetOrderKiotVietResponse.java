package vn.ngong.kiotviet.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetOrderKiotVietResponse {
	private int total;
	private int pageSize;
	private List<DataOrderResponse> data;
}
