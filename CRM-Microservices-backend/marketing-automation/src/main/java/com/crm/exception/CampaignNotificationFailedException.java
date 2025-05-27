package com.crm.exception;

public class CampaignNotificationFailedException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public CampaignNotificationFailedException(String msg) {
		super(msg);
	}
}
