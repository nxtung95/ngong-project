package vn.ngong.helper;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Random;

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

	public static String makeTranxId() {
		// chose a Character random from this String
		String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
				+ "0123456789";

		// create StringBuffer size of AlphaNumericString
		int n = 18;
		StringBuilder sb = new StringBuilder(n);

		for (int i = 0; i < n; i++) {

			// generate a random number between
			// 0 to AlphaNumericString variable length
			int index
					= (int)(AlphaNumericString.length()
					* Math.random());

			// add Character one by one in end of sb
			sb.append(AlphaNumericString
					.charAt(index));
		}

		return sb.toString();
	}

	public static void main(String[] args) {
		System.out.println(makeTranxId());
	}

	public static String formatPostDate(Timestamp postModified) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-yyyy");
		return formatter.format(postModified.toLocalDateTime());
	}
}
