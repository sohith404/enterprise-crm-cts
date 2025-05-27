package com.crm.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class EmailFormat {
	
	String salutation;
	String openingLine;
	String body;
	String conclusion;
	String closing;
	
	@Override
	public String toString() {
		return salutation + "\n\n" +
	           openingLine + "\n\n" +
			   body + "\n\n" +
	           conclusion + "\n\n" +
			   closing;
	}
	
	
}
