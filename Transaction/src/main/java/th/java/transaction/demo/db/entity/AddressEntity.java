package th.java.transaction.demo.db.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "demo_address")
public class AddressEntity {
	
	@Id
	@Column(name = "address_id")
	private Integer id;
	
	@Column(name = "address_detail")
	private String detail;
}
