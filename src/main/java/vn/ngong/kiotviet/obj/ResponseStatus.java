package vn.ngong.kiotviet.obj;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ResponseStatus {
	private String errorCode;
	private String message;
	private List<String> errors;
}
