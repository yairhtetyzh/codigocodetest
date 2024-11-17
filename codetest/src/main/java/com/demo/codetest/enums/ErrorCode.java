package com.demo.codetest.enums;

public enum ErrorCode {
	ERRORCODE_009999("009999", "System error, please try again later"),
	ERRORCODE_020111("020111", "No permission!"),
	ERRORCODE_020124("020124", "Please enter a valid value"),
    ERROR_000000("000000", "Operation is Successful."),
	ERROR_999999("999999", "System Internal Abnormal."),
	ERROR_000001("000001", "User Name %s is Already Exist."),
	ERROR_000002("000002", "Failed to send verification email. Please try again later."),
	ERROR_000003("000003", "Incorrect Old Password."),
	ERROR_000004("000004", "Invalid %s Id."),
	ERROR_000005("000005", "Package is expired."),
	ERROR_000006("000006", "Class(%s) is already open."),
	ERROR_000007("000007", "All Slots in Class(%s) are full.Do you want to include in waitlist?"),
	ERROR_000008("000008", "Login User does not own this User Package."),
	ERROR_000009("000009", "User Package remaining credit is zero."),
	ERROR_000010("000010", "User Package does not have enough credit."),
	ERROR_000011("000011", "Only relative country’s package can book relative country's classes."),
	ERROR_000012("000012", "Unexpected error during booking. Please try again."),
	ERROR_000013("000013", "Login User Already Booked Class(%s)."),
	ERROR_000014("000014", "Login User Already in waiting list to Booked Class(%s)."),
	ERROR_000015("000015", "Booking is Already Cancel."),
	ERROR_000016("000016", "Class is Already Start. Cannot cancel Booking."),
	ERROR_000017("000017", "Invalid User Booking."),
	ERROR_000018("000018", "Cannot CheckIn , Booking Status is (%s)."),
	ERROR_000019("000019", "User cannot checkin, class time is not reached."),
	ERROR_000020("000020", "User cannot checkin, class time is end."),
	ERROR_000021("000021", "JWT token missing."),
	ERROR_000022("000022", "Invalid JWT."),
	ERROR_000023("000023", "Class is not full go to make Booking.");

	private String code;
	private String desc;

	private ErrorCode(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public String getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}
}
