package vn.ngong.helper;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class FormatUtil {
	public static String formatCurrency(BigDecimal data) {
		DecimalFormat formatter = new DecimalFormat("###,###");
		return formatter.format(data);
	}

	public static String formatCurrency(long data) {
		DecimalFormat formatter = new DecimalFormat("###,###");
		return formatter.format(data);
	}
}
