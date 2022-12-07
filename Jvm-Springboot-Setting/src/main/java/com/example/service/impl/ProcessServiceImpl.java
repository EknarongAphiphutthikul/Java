package com.example.service.impl;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bean.LogBean;
import com.example.bean.ReceiveMsgModel;
import com.example.bean.RedisProcessBean;
import com.example.constants.LogConstants.LogLevel;
import com.example.constants.MetricConstants;
import com.example.exception.CustomException;
import com.example.redis.JedisManager;
import com.example.service.ProcessService;
import com.example.utils.CalendarUtil;
import com.example.utils.KafkaSenderUtils;
import com.example.utils.MessageFormatUtil;
import com.example.utils.MetricUtil;
import com.example.utils.TxnLogProcessUtil;

@Service
public class ProcessServiceImpl implements ProcessService {

	@Autowired
	private KafkaSenderUtils kafkaSenderUtils;
	@Autowired
	private JedisManager jedisManager;

	@Override
	public void process(String inputMsg, Date startDateTime, Long commitTime) {
		Date timeStartProcess = CalendarUtil.getCurrentCalendarTimeZoneBangkok().getTime();
		Throwable ex = null;
		LogBean logBean = new LogBean();
		ReceiveMsgModel inputModel = MessageFormatUtil.getStringJsonToObjectWithDateFormat(inputMsg, ReceiveMsgModel.class);
		logBean.initLogRequestService(startDateTime, inputModel.getMsgReceive());
		try {
			RedisProcessBean processBean = new RedisProcessBean();
			processBean.setKeyRedis("MockKey");
			processBean.setValue(inputModel.getMsgReceive());
			
			if (null != processBean) {
				String corIdAndPartition = getDataFromRedis(processBean.getKeyRedis());
				if (null != corIdAndPartition) {
					sendToTopic(inputMsg);
				}
			}
		} catch (Exception e) {
			TxnLogProcessUtil.logInfoLogBean(LogLevel.ERROR, "ProcessService Exception!!", e);
			ex = e;
		} catch (Throwable throwable) {
			TxnLogProcessUtil.logInfoLogBean(LogLevel.ERROR, "ProcessService Throwable!!", throwable);
			ex = throwable;
		} finally {
			if (null == ex) {
				logBean.setLogResponseService(null, null, null);
			} else {
				if (ex instanceof CustomException) {
					CustomException customException = (CustomException) ex;
					logBean.setLogResponseService(null, customException.getErrorDesc(), null);
				} else {
					logBean.setLogResponseServiceException(null, null, ex);
				}
			}
			TxnLogProcessUtil.finish(logBean);
			MetricUtil.log(LogLevel.INFO, timeStartProcess, MetricConstants.PROCESS_TIME_START_FROM_CONSUMER,
					timeStartProcess.getTime() - startDateTime.getTime() - commitTime);
			TxnLogProcessUtil.clearThreadContext();
		}
	}

	private void sendToTopic(String inputMsg) {
		kafkaSenderUtils.sendToTopic(inputMsg);
	}

	private String getDataFromRedis(String key) throws Exception {
		long t1 = System.currentTimeMillis();
		String value = jedisManager.getValue(key);
		MetricUtil.log(LogLevel.INFO, MetricConstants.GET_REDIS, System.currentTimeMillis() - t1);
		if (StringUtils.isNotBlank(value)) {
			return value;
		} else {
			throw new CustomException("Not found Data in redis : key=" + key);
		}
	}
}
