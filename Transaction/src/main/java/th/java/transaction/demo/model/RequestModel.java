package th.java.transaction.demo.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class RequestModel implements Serializable {
	
	private static final long serialVersionUID = 638222824050866760L;
	private String name;
	private String address;
	private int id;
	private String updateDtm;

}
