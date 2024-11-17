package com.demo.codetest.enums;

public enum BookingStatus {

	BOOK(1, "Book"), CHECKIN(2, "CheckIn"), CANCEL(3, "Cancel");

	private Integer code;
	private String desc;

	BookingStatus(Integer code, String desc) {
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
		for (BookingStatus dt : values()) {
			if (dt.getCode() == code) {
				return dt.getDesc();
			}
		}
		return "";
	}
}
