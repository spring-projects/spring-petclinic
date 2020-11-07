package org.springframework.samples.petclinic.model.common;

import java.time.LocalDate;

/**
 * WebSocket message type.
 *
 * @author Paul-Emmanuel DOS SANTOS FACAO
 */
public class WebSocketMessage {

	public static final String ALERT = "alert";

	public static final String SUCCESS = "success";

	public static final String ERROR = "error";

	public static final String WARNING = "warning";

	public static final String INFO = "info";

	private String sender;

	private String time;

	private String content;

	private String type;

	public WebSocketMessage(String content) {
		this.time = LocalDate.now().toString();
		this.content = content;
		this.type = INFO;
	}

	public WebSocketMessage(String content, String type) {
		this.time = LocalDate.now().toString();
		this.content = content;
		this.type = type;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
