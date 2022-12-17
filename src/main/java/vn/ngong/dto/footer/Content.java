package vn.ngong.dto.footer;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Content {
	private String name;
	private String url;
	private List<ContentList> list;
}
