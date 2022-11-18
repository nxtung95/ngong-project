package vn.ngong.kiotviet.obj;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Attribute {
	//private int productId;
	private String key;
	private String name;
	private List<String> value;
}
