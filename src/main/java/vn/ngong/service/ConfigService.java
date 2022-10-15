package vn.ngong.service;

import vn.ngong.entity.SystemParameter;

import java.util.List;

public interface ConfigService {
	List<SystemParameter> getValue(String key);
}
