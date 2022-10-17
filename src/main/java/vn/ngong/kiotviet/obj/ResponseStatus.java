package vn.ngong.kiotviet.obj;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseStatus {
	private String errorCode;
	private String message;
	private String errors;
}
