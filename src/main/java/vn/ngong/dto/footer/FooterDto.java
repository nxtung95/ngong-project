package vn.ngong.dto.footer;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class FooterDto {
	private Logo logo;
	private List<Info> infos;
	private List<Showroom> showrooms;
	private CSKH cskh;
	private Introduce introduce;
	private BuyWhere buyWhere;
	private Object fbConnect;
}
