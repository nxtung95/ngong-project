package vn.ngong.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import vn.ngong.entity.Post;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;

@Repository
@Slf4j
public class CommentNativeRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public double getAvgRate(int productId) {
        try {
            double rate = 0;
            Query query = entityManager.createNativeQuery("SELECT sum(rate) / count(*) FROM `comments` WHERE product_id = :productId");
            query.setParameter("productId", productId);
            BigDecimal tmp = (BigDecimal) query.getSingleResult();
            rate = tmp.doubleValue();
            return rate;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return 0;
    }
}
