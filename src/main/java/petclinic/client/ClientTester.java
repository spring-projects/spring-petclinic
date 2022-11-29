package petclinic.client;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ClientTester {

	private static final Logger logger = LoggerFactory.getLogger(ClientTester.class);
	private static final String BASE_APP_URL = "http://localhost:9876";

	public static void main(String[] args) {
		logger.info("Starting...");
		MyClient myClient = new MyClient(BASE_APP_URL);

		for (int ix = 1; ix <= 5; ix++) {
			myClient.execute("/vets.html");
		}
		for (int ix = 1; ix <= 2; ix++) {
			myClient.execute("/oups", false);
		}
		for (int ix = 1; ix <= 3; ix++) {
			myClient.execute("/appErr1", false);
		}
		for (int ix = 1; ix <= 4; ix++) {
			myClient.execute("/appErr2", false);
		}

		for (int ix = 1; ix <= 2; ix++) {
			myClient.execute("/");
		}

		for (int ix = 1; ix <= 7; ix++) {
			myClient.execute("/owners/find");
		}
		// few of those might return error, since not all ids exists
		for (int ownerId = 1; ownerId <= 20; ownerId++) {
			myClient.execute("/owners/" + ownerId, false);
		}
		for (int ix = 1; ix <= 6; ix++) {
			myClient.execute("/owners?lastName=Davis");
		}
		for (int ix = 1; ix <= 6; ix++) {
			myClient.execute("/owners?lastName=Spring");
		}

		// calling new owner (getting the form) - which has a delay
		for (int ix = 1; ix <= 9; ix++) {
			myClient.execute("/owners/new");
		}

		// using existing owners
		for (int ownerId = 2; ownerId <= 6; ownerId++) {
			// calling edit (getting the form)
			myClient.execute(String.format("owners/%d/edit", ownerId));
			// calling add new pet (getting the form)
			myClient.execute(String.format("owners/%d/pets/new", ownerId));
		}


		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			try {
				logger.info("Shutting down...");
				Thread.sleep(567);
			} catch (InterruptedException e) {
				Thread.interrupted();
			}
		}));
	}

	static class MyClient {

		private final OkHttpClient client;
		private final String baseUrl;

		private MyClient(String baseUrl) {
			this.baseUrl = baseUrl;

			this.client = new OkHttpClient.Builder()
				.build();
		}

		public void execute(String uriPath, boolean throwExceptionWhenCodeIsNot200) {
			Request request = new Request.Builder()
				.url(baseUrl + assureStartsWithSlash(uriPath))
				.build();

			Call call = client.newCall(request);
			try (Response response = call.execute()) {
				if (throwExceptionWhenCodeIsNot200) {
					int code = response.code();
					if (code != 200) {
						throw new RuntimeException(String.format("URI '%s' returned code %d", uriPath, code));
					}
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		public void execute(String uriPath) {
			execute(uriPath, true);
		}

		private static String assureStartsWithSlash(String relativeUri) {
			if (relativeUri == null) {
				return "/";
			}
			String trimmedUri = relativeUri.trim();
			if (trimmedUri.startsWith("/")) {
				return relativeUri;
			}
			return '/' + trimmedUri;
		}
	}
}
