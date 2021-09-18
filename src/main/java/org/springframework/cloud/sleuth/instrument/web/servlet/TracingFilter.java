//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.springframework.cloud.sleuth.instrument.web.servlet;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.cloud.sleuth.CurrentTraceContext;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.SpanCustomizer;
import org.springframework.cloud.sleuth.TraceContext;
import org.springframework.cloud.sleuth.CurrentTraceContext.Scope;
import org.springframework.cloud.sleuth.http.HttpServerHandler;
import org.springframework.cloud.sleuth.http.HttpServerResponse;

public final class TracingFilter implements Filter {

	final ServletRuntime servlet = ServletRuntime.get();

	final CurrentTraceContext currentTraceContext;

	final HttpServerHandler handler;

	public static TracingFilter create(CurrentTraceContext currentTraceContext, HttpServerHandler httpServerHandler) {
		return new TracingFilter(currentTraceContext, httpServerHandler);
	}

	TracingFilter(CurrentTraceContext currentTraceContext, HttpServerHandler httpServerHandler) {
		this.currentTraceContext = currentTraceContext;
		this.handler = httpServerHandler;
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = this.servlet.httpServletResponse(response);
		TraceContext context = (TraceContext) request.getAttribute(TraceContext.class.getName());
		if (context != null) {
			Scope scope = this.currentTraceContext.maybeScope(context);

			try {
				chain.doFilter(request, response);
			}
			finally {
				scope.close();
			}

		}
		else {
			Span span = this.handler.handleReceive(new HttpServletRequestWrapper(req));
			request.setAttribute(SpanCustomizer.class.getName(), span);
			request.setAttribute(TraceContext.class.getName(), span.context());
			TracingFilter.SendHandled sendHandled = new TracingFilter.SendHandled();
			request.setAttribute(TracingFilter.SendHandled.class.getName(), sendHandled);
			Throwable error = null;
			Scope scope = this.currentTraceContext.newScope(span.context());
			boolean var17 = false;
			span.event("adasfdafdsafdsa");
			span.tag("aaaa", "bbbb");

			try {
				var17 = true;
				chain.doFilter(req, res);
				var17 = false;
			}
			catch (Throwable var22) {
				error = var22;
				span.event(String.valueOf(var22));
				throw var22;
			}
			finally {
				if (var17) {
					if (this.servlet.isAsync(req)) {
						this.servlet.handleAsync(this.handler, req, res, span);
					}
					else if (sendHandled.compareAndSet(false, true)) {
						HttpServerResponse responseWrapper = HttpServletResponseWrapper.create(req, res, error);
						this.handler.handleSend(responseWrapper, span);
					}

					scope.close();
				}
			}

			if (this.servlet.isAsync(req)) {
				this.servlet.handleAsync(this.handler, req, res, span);
			}
			else if (sendHandled.compareAndSet(false, true)) {
				HttpServerResponse responseWrapper = HttpServletResponseWrapper.create(req, res, error);
				this.handler.handleSend(responseWrapper, span);
			}

			scope.close();
		}
	}

	public void destroy() {
	}

	public void init(FilterConfig filterConfig) {
	}

	static final class SendHandled extends AtomicBoolean {

		SendHandled() {
		}

	}

}
