package org.springframework.samples.petclinic.service.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.samples.petclinic.dto.common.MessageDTO;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Locale;

@Slf4j
@Service("EmailService")
public class EmailService {

	private static final String EMAIL_TEMPLATE = "email.html";

	private static final String PNG_MIME = "image/png";

	@Value("${mail.background}")
	private String mailBackground;

	@Value("${mail.banner}")
	private String mailBanner;

	@Value("${mail.logo}")
	private String mailLogo;

	@Autowired
	protected JavaMailSender mailSender;

	@Autowired
	protected ITemplateEngine templateEngine;

	/**
	 * sendMailAsynch : for the controller MailController send mail asynchronously
	 * @param messageDTO : message to be send by mail
	 * @param locale : not used now
	 */
	@Async
	public void sendMailAsynch(MessageDTO messageDTO, Locale locale) {
		sendMail(messageDTO, locale);
	}

	/**
	 * sendMail : build mail according to a Thymeleaf template and a MessageDTO object
	 * @param messageDTO : message to be send by mail
	 * @param locale : not used now
	 */
	public void sendMail(MessageDTO messageDTO, Locale locale) {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper message;

		// Prepare the evaluation context
		Context ctx = new Context(locale);
		ctx.setVariable("toFirstName", messageDTO.getFirstName());
		ctx.setVariable("toLastName", messageDTO.getLastName());
		ctx.setVariable("mailSubject", messageDTO.getSubject());
		ctx.setVariable("mailContent", messageDTO.getContent());
		ctx.setVariable("mailLink", messageDTO.getLink());
		ctx.setVariable("mailDate", new Date());

		// Create the HTML body using Thymeleaf
		String output = templateEngine.process(EMAIL_TEMPLATE, ctx);

		try {
			message = new MimeMessageHelper(mimeMessage, true /* multipart */, "UTF-8");

			message.setFrom(messageDTO.getFrom());
			message.setTo(messageDTO.getTo());
			message.setSubject(messageDTO.getSubject());
			message.setText(output, true /* isHtml */);

			message.addInline("mailBackground", new ClassPathResource(mailBackground), PNG_MIME);
			message.addInline("mailBanner", new ClassPathResource(mailBanner), PNG_MIME);
			message.addInline("mailLogo", new ClassPathResource(mailLogo), PNG_MIME);
		}
		catch (MessagingException ex) {
			log.error("Error sending mail !", ex);
		}

		// Send mail
		this.mailSender.send(mimeMessage);
	}

}
