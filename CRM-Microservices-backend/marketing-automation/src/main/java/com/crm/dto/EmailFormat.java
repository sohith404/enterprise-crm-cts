package com.crm.dto;
import lombok.Data;

@Data
public class EmailFormat {

	private String salutation;
	private String openingLine;
	private String body;
	private String conclusion;
	private String closing;
	private String trackingUrl;
	@Override
	public String toString() {
		return salutation + "\n\n" +
				openingLine + "\n\n" +
				body + "\n\n" +
				conclusion + "\n\n" +
				closing+"\n\n"+
				trackingUrl;
	}
}