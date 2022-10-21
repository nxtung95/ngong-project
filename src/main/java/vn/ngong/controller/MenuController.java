package vn.ngong.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import vn.ngong.dto.MenuDto;
import vn.ngong.repository.MenuRepository;
import vn.ngong.response.MenuResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping(value = "/menu")
public class MenuController {
	@Autowired
	private MenuRepository menuRepository;

	@Operation(summary = "API lấy lấy danh sách menu")
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<MenuResponse> findAllMenu() {
		MenuResponse res = MenuResponse.builder()
				.code("00")
				.desc("Success")
				.build();
		List<MenuDto> menuList = menuRepository.findAllMenu();
		if (menuList.isEmpty()) {
			res.setMenuList(menuList);
			return ResponseEntity.ok(res);
		}
		List<MenuDto> returnMenu = new ArrayList<>();
		List<MenuDto> menuLevel1s = menuList.stream().filter(m -> m.getMenuParent() == 0).collect(Collectors.toList());
		for (MenuDto menuDto : menuLevel1s) {
			//load deep
			returnMenu.add(menuDto);
			loadSubMenu(menuDto, menuList);
		}
		res.setMenuList(returnMenu);
		return ResponseEntity.ok(res);
	}

	private void loadSubMenu(MenuDto menuDto, List<MenuDto> menuList) {
		List<MenuDto> menuDtos = menuList.stream().filter(m -> menuDto.getId() == m.getMenuParent()).collect(Collectors.toList());
		if (menuDtos.isEmpty()) {
			return;
		}
		menuDto.setSubMenu(menuDtos);
		for (MenuDto menu : menuDtos) {
			loadSubMenu(menu, menuList);
		}
	}
}
