package vn.ngong.dto.sogao;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UsedSoGaoDto {
	private String usedDate;
	private int usedNumber;
}
