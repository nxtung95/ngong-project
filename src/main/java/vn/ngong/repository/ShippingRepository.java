package vn.ngong.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ngong.entity.ShippingFee;

import java.util.List;

@Repository
public interface ShippingRepository extends JpaRepository<ShippingFee, Integer> {
	List<ShippingFee> findAllByStatus(int status);
}
