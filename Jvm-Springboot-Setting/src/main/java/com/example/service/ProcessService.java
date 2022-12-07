package com.example.service;

import java.util.Date;

public interface ProcessService {

	void process(String inputMsg, Date startDateTime, Long commitTime);

}
