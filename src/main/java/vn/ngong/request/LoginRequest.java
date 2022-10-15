package vn.ngong.request;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class LoginRequest implements Serializable {
	private String username;
	private String password;
}