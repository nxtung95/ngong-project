package vn.ngong.helper;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

public class ValidtionUtils {

	public static boolean checkEmptyOrNull(String... strings) {
		for (String s : strings) {
			if (StringUtils.isBlank(s)) {
				return true;
			}
		}
		return false;
	}

	public static boolean validEmail(String email) {
		String regexPattern = "^(.+)@(.+)$";
		return Pattern.compile(regexPattern).matcher(email).matches();
	}

	public static boolean validPhoneNumber(String phone) {
		return Pattern.compile("^\\d{10,11}$").matcher(phone).matches();
	}
}
