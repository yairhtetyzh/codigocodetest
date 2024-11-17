package com.demo.codetest.exception;

public class CustomWebServiceException extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 7531722738829444483L;
		private String errorCode;
		private String errorMsg;
		private String paramNm;

		/**
		 * Construct with error message.
		 * 
		 * @param message
		 */
		public CustomWebServiceException(String message) {
			super(message);
			this.errorMsg = message;
		}

		/**
		 * Construct with error and inner exception.
		 * 
		 * @param message
		 * @param ex
		 */
		public CustomWebServiceException(String message, Throwable ex) {
			super(message, ex);
		}

		public CustomWebServiceException(String errorCode, String message) {
			super(message);
			this.errorMsg = message;
			this.errorCode = errorCode;
		}

		public CustomWebServiceException(String errorCode, String message, String paramNm) {
			super(message);
			this.errorMsg = message;
			this.errorCode = errorCode;
			this.paramNm = paramNm;
		}

		public String getErrorCode() {
			return errorCode;
		}

		public void setErrorCode(String errorCode) {
			this.errorCode = errorCode;
		}

		public String getErrorMsg() {
			return errorMsg;
		}

		public void setErrorMsg(String errorMsg) {
			this.errorMsg = errorMsg;
		}

		public String getParamNm() {
			return paramNm;
		}

		public void setParamNm(String paramNm) {
			this.paramNm = paramNm;
		}
}

