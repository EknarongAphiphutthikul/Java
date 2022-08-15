package th.java.transaction.demo.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import th.java.transaction.demo.db.entity.AddressEntity;
import th.java.transaction.demo.db.entity.UserEntity;
import th.java.transaction.demo.db.transaction.TransactionByAnnotation;
import th.java.transaction.demo.db.transaction.TransactionByNative;
import th.java.transaction.demo.model.RequestModel;

@RestController
public class Controller {
	
	@Autowired
	private TransactionByAnnotation transactionByAnnotation;
	@Autowired
	private TransactionByNative transactionByNative;
	
	@PostMapping("annotation/insert") 
	public @ResponseBody String insertByTransactionByAnnotation(@RequestBody RequestModel request) throws Exception {
		transactionByAnnotation.saveOrUpdate(createUserEntity(request), createAddressEntity(request));
		return "OK";
	}
	
	@PostMapping("annotation/update") 
	public @ResponseBody String updateByTransactionByAnnotation(@RequestBody RequestModel request) throws Exception {
		transactionByAnnotation.saveOrUpdate(createUserEntity(request), createAddressEntity(request));
		return "OK";
	}
	
	
	@PostMapping("native/insert") 
	public @ResponseBody String insertByTransactionByNative(@RequestBody RequestModel request) throws Exception {
		transactionByNative.save(createUserEntity(request), createAddressEntity(request));
		return "OK";
	}
	
	@PostMapping("native/update") 
	public @ResponseBody String updateByTransactionByNative(@RequestBody RequestModel request) throws Exception {
		transactionByNative.update(createUserEntity(request), createAddressEntity(request));
		return "OK";
	}
	
	private UserEntity createUserEntity(RequestModel request) throws ParseException {
		UserEntity entity = new UserEntity();
		entity.setId(request.getId());
		entity.setName(request.getName());
		entity.setUpdateDtm(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(request.getUpdateDtm()));
		return entity;
	}
	
	private AddressEntity createAddressEntity(RequestModel request) {
		AddressEntity entity = new AddressEntity();
		entity.setId(request.getId());
		entity.setDetail(request.getAddress());
		return entity;
	}

}
