package org.springframework.samples.petclinic.model.common;


import java.time.LocalDate;

public class WebSocketMessage {

	private String sender;

	private String time;

	private String content;


	public WebSocketMessage(String content) {
		this.time = LocalDate.now().toString();
		this.content = content;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
