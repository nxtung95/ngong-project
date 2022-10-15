package vn.ngong.helper;

import org.apache.commons.lang3.StringUtils;

public class ValidtionUtils {
	public static boolean checkEmptyOrNull(String... strings) {
		for (String s : strings) {
			if (StringUtils.isBlank(s)) {
				return true;
			}
		}
		return false;
	}
}
