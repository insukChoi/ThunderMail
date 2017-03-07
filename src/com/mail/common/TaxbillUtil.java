package com.mail.common;

public class TaxbillUtil {
	
	public TaxbillUtil(){
		
	}

	//================================================================================
	// @descript : 세금계산서 코드
	//================================================================================
	public String getStrTaxCode(String tax_type){
		String strTaxType = "";
		if (tax_type == null ||  "".equals(tax_type))   return strTaxType = "";

		String tax_code = "";
		String tax_div = "";
		if(tax_type.length() == 4) 	{
			tax_code = tax_type.substring(0,2);	
			tax_div = tax_type.substring(2);	
		}

		if("01".equals(tax_code))			strTaxType = "세금계산서";
		else if("02".equals(tax_code))		strTaxType = "수정세금계산서";
		else if("03".equals(tax_code))		strTaxType = "계산서";
		else if("04".equals(tax_code))		strTaxType = "수정계산서";
		else if("05".equals(tax_code))		strTaxType = "영수증";
		else								strTaxType = tax_code;

		return getStrTaxDiv(tax_div) + " " + strTaxType;
	}

	//================================================================================
	// @descript : 세금계산서 분류
	//================================================================================
	public String getStrTaxDiv(String tax_type){
		String strTaxType = "";
		if (tax_type == null ||  "".equals(tax_type))   return strTaxType = "";

		if(tax_type.length() == 4) 		tax_type = tax_type.substring(2);	

		if("03".equals(tax_type))		strTaxType = "위수탁";

		return strTaxType;
	}

}
