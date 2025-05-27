package com.crm.exception;
public class DuplicateCampaignNameException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public DuplicateCampaignNameException(String msg) {
		super(msg);
	}
	
}
