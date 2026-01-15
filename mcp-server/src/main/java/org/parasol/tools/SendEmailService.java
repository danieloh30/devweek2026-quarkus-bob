package org.parasol.tools;

import jakarta.enterprise.context.ApplicationScoped;

import io.quarkiverse.mcp.server.ToolResponse;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;

import dev.langchain4j.agent.tool.Tool;

@ApplicationScoped
public class SendEmailService {
  private final Mailer mailer;
  private final Tracer tracer;

  public SendEmailService(Mailer mailer, Tracer tracer) {
    this.mailer = mailer;
    this.tracer = tracer;
  }

  @Tool(name = "sendEmail", value = "Sends an email using the Quarkus Mailer")
  public ToolResponse sendEmail(Email email) {
    Span span = tracer.spanBuilder("sendEmail")
        .setSpanKind(SpanKind.INTERNAL)
        .setParent(Context.current())
        .startSpan();

    try {
      span.setAttribute("email.to", email.to());
      span.setAttribute("email.from", email.from());
      span.setAttribute("email.subject", email.subject());
      span.setAttribute("email.body.length", email.body().length());

      mailer.send(
          Mail.withText(email.to(), email.subject(), email.body())
              .setFrom(email.from())
      );

      span.setStatus(StatusCode.OK);
      span.addEvent("Email sent successfully");

      return ToolResponse.success("Email successfully sent");
    } catch (Exception e) {
      span.setStatus(StatusCode.ERROR, "Failed to send email");
      span.recordException(e);
      return ToolResponse.error("Failed to send email: " + e.getMessage());
    } finally {
      span.end();
    }
  }
}

// Made with Bob
