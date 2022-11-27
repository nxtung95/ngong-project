package vn.ngong.kiotviet.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
public class CreateCustomerRequest {
	private String code;
	private String name;
	private boolean gender;
	private Date birthDate;
	private String contactNumber;
	private String address;
	private String email;
	private String comments;
	private int[] groupIds;
	private int branchId;
	private String source;
}
