package com.crm.exception;
public class CampaignNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public CampaignNotFoundException(String msg) {
		super(msg);
	}
}
