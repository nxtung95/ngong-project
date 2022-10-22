package vn.ngong.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestTransCustomerDto {
	private String cusName;
	private String cusEmail;
	private String cusPhone;
	private String cusCity;
	private String cusDistrict;
	private String cusWard;
	private String cusNote;
}
