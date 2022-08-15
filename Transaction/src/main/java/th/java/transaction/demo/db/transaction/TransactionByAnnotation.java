package th.java.transaction.demo.db.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import th.java.transaction.demo.db.entity.AddressEntity;
import th.java.transaction.demo.db.entity.UserEntity;
import th.java.transaction.demo.db.repository.AddressRepo;
import th.java.transaction.demo.db.repository.UserRepo;

@Component
public class TransactionByAnnotation {

	@Autowired
	private AddressRepo addressRepo;
	@Autowired
	private UserRepo userRepo;
	
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	public void saveOrUpdate(UserEntity userEntity, AddressEntity addressEntity) throws Exception {
		userRepo.saveAndFlush(userEntity);
		addressRepo.save(addressEntity);
	}
}
