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
public class ReceiveMsgModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1949435241443264502L;
	private String msgReceive;
}
