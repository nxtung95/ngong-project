package vn.ngong.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import vn.ngong.entity.Ship;
import vn.ngong.helper.FormatUtil;
import vn.ngong.response.ProductFilterDetail;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class ShipRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public int getShipPrice(int cityCode, int districtCode, int weight, int totalPrice) {
        try {
            Query query = entityManager.createNativeQuery("SELECT s.id, s.price" +
                    " FROM ships s " +
                    " INNER JOIN ship_areas a ON a.id = s.ship_area_id" +
                    " WHERE s.`status` = 1" +
                    " AND a.`status` = 1" +
                    " AND a.city_code = " + cityCode +
                    " AND a.district_code = " + districtCode +
                    " AND ((s.start_kg = 0 AND s.end_kg = 0) OR (s.end_kg = 0 AND s.start_kg <= " + weight + ") OR (" + weight + " BETWEEN s.end_kg AND s.start_kg))" +
                    " AND ((s.start_money = 0 AND s.end_money = 0) OR (s.end_money = 0 AND s.start_money <= " + totalPrice + ") OR (" + totalPrice + " BETWEEN s.end_money AND s.start_money))" +
                    " LIMIT 1");
            List<Object[]> objects = query.getResultList();
            List<Ship> ships = new ArrayList<>();
            for (Object[] obj : objects) {
                ships.add(Ship.builder()
                        .id(Integer.parseInt((obj[0]).toString()))
                        .price(Integer.parseInt(obj[1].toString()))
                        .build());
            }

            return ships.get(0).getPrice();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return 0;
    }
}
