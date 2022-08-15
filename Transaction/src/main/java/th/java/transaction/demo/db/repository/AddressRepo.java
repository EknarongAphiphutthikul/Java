package th.java.transaction.demo.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import th.java.transaction.demo.db.entity.AddressEntity;

@Repository
public interface AddressRepo extends JpaRepository<AddressEntity, Integer> {

}
