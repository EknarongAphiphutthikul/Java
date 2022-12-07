package com.example.bean;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SendMsgModel implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 4628974560719635034L;
	private String msgSend;

}
