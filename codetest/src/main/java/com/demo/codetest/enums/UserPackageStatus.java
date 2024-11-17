package com.demo.codetest.enums;

public enum UserPackageStatus {

	AVAILABLE(1, "Available"), EXPIRED(2, "Expired");

	private Integer code;
	private String desc;

	UserPackageStatus(Integer code, String desc) {
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
		for (UserPackageStatus dt : values()) {
			if (dt.getCode() == code) {
				return dt.getDesc();
			}
		}
		return "";
	}
}
