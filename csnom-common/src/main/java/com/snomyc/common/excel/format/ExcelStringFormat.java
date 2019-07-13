package com.snomyc.common.excel.format;

public class ExcelStringFormat implements ExcelFormat{

	public Object format(Object obj) {
		return obj.toString();
	}

}
