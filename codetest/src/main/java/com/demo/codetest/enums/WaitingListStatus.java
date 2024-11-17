package com.demo.codetest.enums;

public enum WaitingListStatus {

	WAITING(1, "Waiting"), BOOKED(2, "Booked"), REFUNDED(3, "Refunded");

	private Integer code;
	private String desc;

	WaitingListStatus(Integer code, String desc) {
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
		for (WaitingListStatus dt : values()) {
			if (dt.getCode() == code) {
				return dt.getDesc();
			}
		}
		return "";
	}
}
