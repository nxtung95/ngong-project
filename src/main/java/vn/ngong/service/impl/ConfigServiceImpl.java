package vn.ngong.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.ngong.entity.SystemParameter;
import vn.ngong.service.ConfigService;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ConfigServiceImpl implements ConfigService {
	private List<SystemParameter> systemParameters = new ArrayList<>();

	@PostConstruct
	public void init() {
		systemParameters.add(SystemParameter.builder().key("hotline").value("09123465").status("1").order(1).build());
		systemParameters.add(SystemParameter.builder().key("slide").image("slide1.img").status("1").value("Slide header").order(1).build());
		systemParameters.add(SystemParameter.builder().key("slide").image("slide2.img").status("1").value("Slide header").order(2).build());
		systemParameters.add(SystemParameter.builder().key("slide").image("slide3.img").status("1").value("Slide header").order(3).build());
	}

	@Override
	public List<SystemParameter> getValue(String key) {
		return systemParameters.stream().filter(s -> key.equals(s.getKey())).collect(Collectors.toList());
	}
}
