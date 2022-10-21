package vn.ngong.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class MenuDto {
	private int id;
	private int termTaxanomyId;
	private String postTitle;
	private String postName;
	private int menuOrder;
	private String nName;
	private String nTitle;
	private String menuLink;
	private int menuParent;
	private String type;
	private String name;
	private String slug;
	private List<MenuDto> subMenu;

}
