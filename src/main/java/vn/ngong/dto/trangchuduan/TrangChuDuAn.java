package vn.ngong.dto.trangchuduan;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import vn.ngong.dto.chuyendicuangong.ImageBanner;
import vn.ngong.entity.Project;

import java.util.List;

@Getter
@Setter
@Builder
public class TrangChuDuAn {
	private ImageBanner banner;
	private String moDau;
	private List<Project> projectList;
}
