package vn.ngong.helper;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FormatUtil {
	public static String formatCurrency(BigDecimal data) {
		DecimalFormat formatter = new DecimalFormat("###,###");
		return formatter.format(data);
	}

	public static String formatCurrency(long data) {
		DecimalFormat formatter = new DecimalFormat("###,###");
		return formatter.format(data);
	}
	public static Timestamp stringToTimestamp(String input) throws ParseException {
		if (input != null || input == "") {
			return new Timestamp(0);
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
		Date parsedDate = dateFormat.parse(input);
		Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());

		return timestamp;
	}
}
