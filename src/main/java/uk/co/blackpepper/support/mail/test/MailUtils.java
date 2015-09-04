package uk.co.blackpepper.support.mail.test;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;

public final class MailUtils {
	
	private MailUtils() {
		throw new AssertionError();
	}
	
	public static MimeMessage newMimeMessage() {
		return new MimeMessage((Session) null);
	}
}
