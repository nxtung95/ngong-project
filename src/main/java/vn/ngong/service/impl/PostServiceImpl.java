package vn.ngong.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.ngong.entity.Menu;
import vn.ngong.entity.Post;
import vn.ngong.service.PostService;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PostServiceImpl implements PostService {
	private List<Post> postList = new ArrayList<>();
	private List<Menu> menuList = new ArrayList<>();

	@PostConstruct
	public void init() {
		menuList.add(Menu.builder().id(1).code("SO_LUOC_VE_NGONG").name("Sơ lược về ngỗng").build());
		menuList.add(Menu.builder().id(2).code("BAO_CHI_NOI_GI_VE_NGONG").name("Báo chí nói gì về ngỗng").build());
		menuList.add(Menu.builder().id(3).code("HE_THONG_SX_VA_QL").name("Hệ thống sản xuất và quản lý").build());
		menuList.add(Menu.builder().id(4).code("CHUYEN_DI_CUA_NGONG").name("Chuyến đi của Ngỗng").build());
		menuList.add(Menu.builder().id(5).code("DU_AN_KHAC").name("Dự án khác").build());
		menuList.add(Menu.builder().id(6).code("CHUYEN_NGONG").name("Chuyện Ngỗng").build());
		menuList.add(Menu.builder().id(7).code("CHUYEN_XUONG").name("Chuyện xưởng").build());
		menuList.add(Menu.builder().id(8).code("CHUYEN_VUON").name("Chuyện vườn").build());
		menuList.add(Menu.builder().id(9).code("CHUYEN_TIEU_DUNG").name("Chuyện tiêu dùng").build());
		menuList.add(Menu.builder().id(10).code("CHINH_SACH_DAI_LY_CTV").name("Chính sách đại lý cộng tác viên").build());
		menuList.add(Menu.builder().id(11).code("HE_THONG_DOI_TAC").name("Hệ thống đối tác").build());
		menuList.add(Menu.builder().id(12).code("TUYEN_DUNG").name("Tuyển dụng").build());
		menuList.add(Menu.builder().id(13).code("LIEN_HE").name("Liên hệ").build());
		menuList.add(Menu.builder().id(14).code("MUA_O_DAU").name("Mua ở đâu").build());

		// VE NGONG
		postList.add(Post.builder()
				.id(1)
				.isHtml(true)
				.contentHtml("Sample so luoc ve ngong")
				.menuId(1)
				.order(1)
				.status(1)
				.build());
		postList.add(Post.builder()
				.id(2)
				.isHtml(true)
				.contentHtml("Sample bao chi noi gi ve ngong")
				.menuId(2)
				.order(1)
				.status(1)
				.build());
		postList.add(Post.builder()
				.id(3)
				.isHtml(true)
				.contentHtml("Sample Hệ thống sản xuất và quản lý")
				.menuId(3)
				.order(1)
				.status(1)
				.build());

		// Chuyen di
		postList.add(Post.builder()
				.id(4)
				.isHtml(true)
				.contentHtml("Chuyến đi của Ngỗng")
				.menuId(4)
				.order(1)
				.status(1)
				.build());
		postList.add(Post.builder()
				.id(5)
				.isHtml(true)
				.contentHtml("Dự án khác")
				.menuId(5)
				.order(1)
				.status(1)
				.build());

		StringBuffer buffer = new StringBuffer();
		try {
			// File path is passed as parameter
			File file = new File(
					"sample/chuyen_cua_ngong.html");
			// Note:  Double backquote is to avoid compiler
			// interpret words
			// like \test as \t (ie. as a escape sequence)

			// Creating an object of BufferedReader class
			BufferedReader br
					= new BufferedReader(new FileReader(file));

			// Condition holds true till
			// there is character in a string
			String line = br.readLine();
			while (line != null) {
				buffer.append(line);
				line = br.readLine();
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		// Chuyen Ngong
		postList.add(Post.builder()
				.id(6)
				.isHtml(true)
				.contentHtml(buffer.toString())
				.menuId(6)
				.order(1)
				.status(1)
				.build());
		postList.add(Post.builder()
				.id(7)
				.isHtml(true)
				.contentHtml(buffer.toString())
				.menuId(6)
				.order(2)
				.status(1)
				.build());
		postList.add(Post.builder()
				.id(8)
				.isHtml(true)
				.contentHtml(buffer.toString())
				.menuId(6)
				.order(3)
				.status(1)
				.build());
		postList.add(Post.builder()
				.id(9)
				.isHtml(true)
				.contentHtml("Chuyện xưởng 1")
				.menuId(7)
				.order(1)
				.status(1)
				.build());
		postList.add(Post.builder()
				.id(10)
				.isHtml(true)
				.contentHtml("Chuyện vườn 1")
				.menuId(8)
				.order(1)
				.status(1)
				.build());
		postList.add(Post.builder()
				.id(11)
				.isHtml(true)
				.contentHtml("Chuyện tiêu dùng 1")
				.menuId(9)
				.order(1)
				.status(1)
				.build());

		// Hop tac
		postList.add(Post.builder()
				.id(12)
				.isHtml(true)
				.contentHtml("Chính sách đại lý cộng tác viên")
				.menuId(10)
				.order(1)
				.status(1)
				.build());
		postList.add(Post.builder()
				.id(13)
				.isHtml(true)
				.contentHtml("Hệ thống đối tác")
				.menuId(11)
				.order(1)
				.status(1)
				.build());
		postList.add(Post.builder()
				.id(12)
				.isHtml(true)
				.contentHtml("Tuyển dụng")
				.menuId(12)
				.order(1)
				.status(1)
				.build());

		// Liên hệ
		postList.add(Post.builder()
				.id(13)
				.isHtml(true)
				.contentHtml("Liên hệ")
				.menuId(5)
				.order(1)
				.status(1)
				.build());

		// Mua ở đâu
		postList.add(Post.builder()
				.id(14)
				.isHtml(true)
				.contentHtml("Tuyển dụng")
				.menuId(5)
				.order(1)
				.status(1)
				.build());
	}

	@Override
	public List<Post> findAllPostByMenu(String menuCode) {
		Menu menu = menuList.stream().filter(m -> menuCode.equals(m.getCode())).findFirst().orElse(null);
		if (menu == null) {
			return new ArrayList<>();
		}
		return postList.stream().filter(p -> menu.getId() == p.getMenuId()).collect(Collectors.toList());
	}
}
