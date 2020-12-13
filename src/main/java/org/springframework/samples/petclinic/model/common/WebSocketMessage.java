package org.springframework.samples.petclinic.model.common;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * WebSocket message type.
 *
 * @author Paul-Emmanuel DOS SANTOS FACAO
 */
@Getter
@Setter
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

}
