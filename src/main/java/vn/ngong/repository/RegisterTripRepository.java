package vn.ngong.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ngong.entity.RegisterTrip;

@Repository
public interface RegisterTripRepository extends JpaRepository<RegisterTrip, Integer> {
}
