package th.java.transaction.demo.db.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Data
@Entity
@Table(name = "demo_user")
public class UserEntity {
	
	@Id
	@Column(name = "user_id")
	private Integer id;
	
	@Column(name = "user_name")
	private String	name;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updateDtm")
	private Date updateDtm;
}
