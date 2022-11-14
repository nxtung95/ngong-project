package vn.ngong.kiotviet.obj;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Attribute {
	private int productId;
	private String attributeName;
	private String attributeValue;
}
