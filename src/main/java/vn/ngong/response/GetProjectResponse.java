package vn.ngong.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.ngong.entity.Project;
import vn.ngong.entity.RegisterProject;

import java.util.List;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
public class GetProjectResponse extends BaseResponse {
	private List<Project> projectList;
}
