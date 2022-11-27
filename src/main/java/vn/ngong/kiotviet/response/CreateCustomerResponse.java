package vn.ngong.kiotviet.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateCustomerResponse {
	private String message;
	private DataCustomerResponse data;
}
