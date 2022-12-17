package vn.ngong.dto.sogao;

import java.sql.Timestamp;

public interface UserSoGaoHistoryDto {
	String getSoGaoCode();
	String getSoGaoName();
	Timestamp getPurchaseDate();
	Timestamp getCreatedAt();
	int getUsedNumber();
	int getRemainingNumber();
}
