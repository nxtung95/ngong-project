package vn.ngong.dto.footer;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BuyWhere {
	private String title;
	private List<Content> content;
}
