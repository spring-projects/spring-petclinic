package org.springframework.samples.petclinic;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.exporter.otlp.http.trace.OtlpHttpSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import io.opentelemetry.sdk.trace.export.SpanExporter;
import io.opentelemetry.sdk.trace.samplers.Sampler;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Stream;



public class OtelExporter {
	public static void initOpenTelemetry() throws IOException {
		Resource serviceName = Optional.ofNullable(System.getenv("petClinic"))
			.map(n -> Attributes.of(AttributeKey.stringKey("springPetClinic"), n))
			.map(Resource::create)
			.orElseGet(Resource::empty);

		Resource envResourceAttributes = Resource.create(Stream.of(Optional.ofNullable(System.getenv("OTEL_RESOURCE_ATTRIBUTES")).orElse("").split(","))
			.filter(pair -> pair != null && pair.length() > 0 && pair.contains("="))
			.map(pair -> pair.split("="))
			.filter(pair -> pair.length == 2)
			.collect(Attributes::builder, (b, p) -> b.put(p[0], p[1]), (b1, b2) -> b1.putAll(b2.build()))
			.build()
		);


		Resource dtMetadata = Resource.empty();
		for (String name : new String[] {"dt_metadata_e617c525669e072eebe3d0f08212e8f2.properties", "/var/lib/dynatrace/enrichment/dt_metadata.properties"}) {
			try {
				Properties props = new Properties();
				props.load(name.startsWith("/var") ? new FileInputStream(name) : new FileInputStream(Files.readAllLines(Paths.get(name)).get(0)));
				dtMetadata = dtMetadata.merge(Resource.create(props.entrySet().stream()
					.collect(Attributes::builder, (b, e) -> b.put(e.getKey().toString(), e.getValue().toString()), (b1, b2) -> b1.putAll(b2.build()))
					.build())
				);
			} catch (IOException e) {}
		}


		SpanExporter exporter = OtlpHttpSpanExporter.builder()
			.setEndpoint("http://englab-tdbank.datadog.hq.com") //TODO Replace <URL> to your SaaS/Managed-URL as mentioned in the next step
			.addHeader("Authorization", "93ffd05f8f2a91e0b8a22e476e1bb3c6a3de5dcd") //TODO Replace <TOKEN> with your API Token as mentioned in the next step
			.build();

		SdkTracerProvider sdkTracerProvider = SdkTracerProvider.builder()
			.setResource(Resource.getDefault().merge(envResourceAttributes).merge(serviceName).merge(dtMetadata))
			.setSampler(Sampler.alwaysOn())
			.addSpanProcessor(BatchSpanProcessor.builder(exporter).build())
			.build();

		OpenTelemetrySdk.builder()
			.setTracerProvider(sdkTracerProvider)
			.setPropagators(ContextPropagators.create(W3CTraceContextPropagator.getInstance()))
			.buildAndRegisterGlobal();
		Runtime.getRuntime().addShutdownHook(new Thread(sdkTracerProvider::close));
	}
}
