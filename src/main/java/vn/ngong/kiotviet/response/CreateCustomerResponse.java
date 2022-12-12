package vn.ngong.kiotviet.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import vn.ngong.kiotviet.obj.ResponseStatus;

@Getter
@Setter
@Builder
public class CreateCustomerResponse {
	private String message;
	private DataCustomerResponse data;
}
