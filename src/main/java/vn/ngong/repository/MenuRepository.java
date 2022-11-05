package vn.ngong.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import vn.ngong.dto.MenuDto;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class MenuRepository {
	@PersistenceContext
	private EntityManager entityManager;

	public List<MenuDto> findAllMenu() {
		try {
			List<MenuDto> returnMenus = new ArrayList<>();
			Query query = entityManager.createNativeQuery("select i.id, i.term_taxonomy_id, i.post_title, i.post_name, i.menu_order, i.n_name, i.n_title, i.menu_link, i.menu_parent, i.type, i.name, i.slug from " +
					" (select d.*,e.name,e.slug from( SELECT p.id,txr.term_taxonomy_id, p.post_title, p.post_name, p.menu_order, n.post_name as n_name, n.post_title as n_title, m.meta_value as menu_link, pp.meta_value as menu_parent,pt.meta_value as type" +
					" FROM wp_term_relationships as txr INNER JOIN wp_posts as p ON txr.object_id = p.ID LEFT JOIN wp_postmeta as m ON p.ID = m.post_id LEFT JOIN wp_postmeta as pl ON p.ID = pl.post_id AND pl.meta_key = '_menu_item_object_id' " +
					" LEFT JOIN wp_postmeta as pp ON p.ID = pp.post_id AND pp.meta_key = '_menu_item_menu_item_parent' " +
					" LEFT JOIN wp_postmeta as pt ON p.ID = pt.post_id AND pt.meta_key = '_menu_item_object' " +
					" LEFT JOIN wp_posts as n ON pl.meta_value = n.ID WHERE p.post_status='publish' AND p.post_type = 'nav_menu_item' AND m.meta_key = '_menu_item_url') d " +
					" LEFT JOIN wp_terms as e on d.term_taxonomy_id=e.term_id) i where i.term_taxonomy_id = (SELECT term_id FROM wp_terms where slug = 'main-menu'" +
					" ) ORDER BY i.menu_order ASC");
			List<Object[]> objects = query.getResultList();
			for (Object[] obj : objects) {
				returnMenus.add(MenuDto.builder()
						.id(Integer.parseInt((obj[0]).toString()))
						.termTaxanomyId(Integer.parseInt(obj[1].toString()))
						.postTitle((String) obj[2])
						.postName((String) obj[3])
						.menuOrder(Integer.parseInt(obj[4].toString()))
						.nName((String) obj[5])
						.nTitle((String) obj[6])
						.menuLink((String) obj[7])
						.menuParent(Integer.parseInt(obj[8].toString()))
						.type((String) obj[9])
						.name((String) obj[10])
						.slug((String) obj[11])
						.build());
			}
			return returnMenus;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return new ArrayList<>();
	}

	public Map<Integer, String> findAllImageRepresent() {
		try {
			Map<Integer, String> mapImage = new HashMap<>();
			Query query = entityManager.createNativeQuery("SELECT pm.post_id as parentPostId, p.guid " +
					" FROM wp_posts p LEFT JOIN wp_postmeta pm ON p.ID = pm.meta_value AND pm.meta_key = '_thumbnail_id'");
			List<Object[]> objects = query.getResultList();
			for (Object[] obj : objects) {
				if (obj[0] != null) {
					int parentPostId = Integer.parseInt(obj[0].toString());
					String image = obj[1].toString();
					mapImage.put(parentPostId, image);
				}
			}
			return mapImage;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return new HashMap<>();
	}

	public Map<Integer, String> findAllDescription() {
		try {
			Map<Integer, String> mapDescription = new HashMap<>();
			Query query = entityManager.createNativeQuery("SELECT post_id as parentPostId, meta_value as description FROM `wp_postmeta` WHERE `meta_key` = 'description'");
			List<Object[]> objects = query.getResultList();
			for (Object[] obj : objects) {
				if (obj[0] != null) {
					int parentPostId = Integer.parseInt(obj[0].toString());
					String description = obj[1].toString();
					mapDescription.put(parentPostId, description);
				}
			}
			return mapDescription;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return new HashMap<>();
	}
}
