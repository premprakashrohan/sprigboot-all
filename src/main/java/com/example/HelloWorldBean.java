package com.example;

public class HelloWorldBean {
	String message = null;

	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public HelloWorldBean() {

	}
	public HelloWorldBean(String message) {
		this.message = message;
	}
}
