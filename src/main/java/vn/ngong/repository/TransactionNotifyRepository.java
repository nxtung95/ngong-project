package vn.ngong.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ngong.entity.TransactionNotify;

import java.util.List;

@Repository
public interface TransactionNotifyRepository extends JpaRepository<TransactionNotify, Integer> {
	List<TransactionNotify> findAllByUserIdOrderByUpdatedDateDesc(int userId);
}
