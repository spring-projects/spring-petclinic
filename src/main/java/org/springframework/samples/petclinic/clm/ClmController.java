package org.springframework.samples.petclinic.clm;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Segment;
import com.newrelic.api.agent.Trace;
import com.newrelic.api.agent.TransactionNamePriority;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Date;

@Controller
public class ClmController {

	@Autowired
	private OwnerRepository ownerRepository;

	/**
	 * This method demonstrates an auto instrumented method.
	 */
	@GetMapping("/clm/auto-only")
	public String autoOnly(Model model) {
		setMessage(model, "Java/org.springframework.samples.petclinic.clm.ClmController/autoOnly");
		doWait();
		return "welcome";
	}


	/**
	 * Besides the auto instrumentation, this also demonstrates a manual trace created by an annotation.
	 */
	@GetMapping("/clm/annotation")
	public String annotation(Model model) {
		setMessage(model, "Java/org.springframework.samples.petclinic.clm.ClmController/annotation");
		annotatedMethod();
		return "welcome";
	}

	@Trace
	private void annotatedMethod() {
		doWait();
	}


	/**
	 * Besides the auto instrumentation, this also demonstrates a manual trace created using the API.
	 */
	@GetMapping("/clm/api")
	public String api(Model model) {
		setMessage(model, "Java/org.springframework.samples.petclinic.clm.ClmController/api");
		apiMethod();
		return "welcome";
	}

	private void apiMethod() {
		Segment segment = NewRelic.getAgent().getTransaction().startSegment("segmentCreatedByTheApi");
		doWait();
		segment.end();
	}


	/**
	 * Besides the auto instrumentation, this also demonstrates a trace created by XML instrumentation.
	 */
	@GetMapping("/clm/xml")
	public String xml(Model model) {
		setMessage(model, "Java/org.springframework.samples.petclinic.clm.ClmController/xml");
		xmlMethod();
		return "welcome";
	}

	private void xmlMethod() {
		doWait();
	}


	/**
	 * Besides the auto instrumentation, this also demonstrates a trace in a static method.
	 */
	@GetMapping("/clm/static")
	public String staticRequest(Model model) {
		setMessage(model, "Java/org.springframework.samples.petclinic.clm.ClmController/static");
		staticMethod();
		return "welcome";
	}

	@Trace
	private static void staticMethod() {
		doWait();
	}


	/**
	 * Besides the auto instrumentation, this also demonstrates a trace that contains an Http request.
	 */
	@GetMapping("/clm/http")
	public String http(Model model) {
		setMessage(model, "Java/org.springframework.samples.petclinic.clm.ClmController/http");
		httpMethod();
		return "welcome";
	}

	@Trace
	private void httpMethod() {
		try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
			HttpGet get = new HttpGet("http://example.com");
			try (CloseableHttpResponse execute = httpClient.execute(get)) {
				StatusLine statusLine = execute.getStatusLine();
				System.out.println(statusLine);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}


	/**
	 * Besides the auto instrumentation, this also demonstrates a trace that has a db call.
	 */
	@GetMapping("/clm/db")
	public String db(Model model) {
		setMessage(model, "Java/org.springframework.samples.petclinic.clm.ClmController/db");
		dbMethod();
		return "welcome";
	}

	@Trace
	private void dbMethod() {
		ownerRepository.findAll(Pageable.ofSize(100000));
	}


	private void setMessage(Model model, String spanName) {
		model.addAttribute("message",
			new Date() + ": Look for a span named \"" + spanName+ "\" and its children.");

	}

	/**
	 * Simple method that waits so the spans take some time to execute.
	 */
	private static void doWait() {
		try {
			Thread.sleep(200L);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	@PostConstruct
	@Trace(dispatcher = true)
	void init() {
		doWait();
	}
}
