package com.demo.codetest.enums;

public enum RefundStatus {

	REFUNDED(1, "Refunded"), NOT_REFUNDED(2, "Not Refunded");

	private Integer code;
	private String desc;

	RefundStatus(Integer code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public Integer getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}

	public static String getDescByCode(Integer code) {
		for (RefundStatus dt : values()) {
			if (dt.getCode() == code) {
				return dt.getDesc();
			}
		}
		return "";
	}
}
