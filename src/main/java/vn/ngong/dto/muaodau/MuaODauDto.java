package vn.ngong.dto.muaodau;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class MuaODauDto {
	private String title;
	private SaleOnline saleOnline;
	private List<Stock> stocks;
	private List<LocationSaleOnline> locationSaleOnline;
}
