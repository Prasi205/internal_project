package com.tm.internalproject.pojo.response;

public class ResponsePojo {

	private String message;
	private Object responseData;
	private Boolean isSuccess;

	public void response(String message, Object responseData, boolean isSuccess) {
		setMessage(message);
		setResponseData(responseData);
		setIsSuccess(isSuccess);
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public Object getResponseData() {
		return responseData;
	}

	public void setResponseData(Object responseData) {
		this.responseData = responseData;
	}

	public Boolean getIsSuccess() {
		return isSuccess;
	}

	public void setIsSuccess(Boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

}