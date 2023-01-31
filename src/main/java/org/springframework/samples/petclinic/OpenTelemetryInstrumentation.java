
package org.springframework.samples.petclinic;
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;


public class OpenTelemetryInstrumentation {

    public static void instrumentTracing() {
        Tracer tracer = GlobalOpenTelemetry
            .getTracerProvider()
            .tracerBuilder("my-tracer") //TODO Replace with the name of your tracer
            .build();

        Span span = tracer
            .spanBuilder("my-span") //TODO Replace with the name of your span
            .setAttribute("my-key-1", "my-value-1") //TODO Add initial attributes
            .startSpan();

        try (Scope scope = span.makeCurrent()) {
            span.setAttribute("key-2", "value-2"); //TODO Add extra attributes if necessary
            //TODO your code goes here
        } finally {
            span.end();

        }

	}
}
