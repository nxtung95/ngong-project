package vn.ngong.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ngong.entity.Orders;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Integer> {
}
