package uk.co.blackpepper.support.mail.test;

import java.io.IOException;

import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static java.util.Arrays.asList;

import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.hasItems;

import static uk.co.blackpepper.support.mail.test.InternetAddressMatcher.internetAddresses;

public class MimeMessageMatcher extends TypeSafeMatcher<MimeMessage> {

	private final MimeMessage expected;
	
	private final boolean matchContentUsingRegEx;

	public MimeMessageMatcher(MimeMessage expected) {
		this(expected, false);
	}
	
	private MimeMessageMatcher(MimeMessage expected, boolean matchContentUsingRegEx) {
		this.expected = expected;
		this.matchContentUsingRegEx = matchContentUsingRegEx;
	}
	
	public static Matcher<MimeMessage> mimeMessage(MimeMessage expected) {
		return new MimeMessageMatcher(expected);
	}
	
	public static Matcher<MimeMessage> mimeMessageWithContentRegExMatch(MimeMessage expected) {
		return new MimeMessageMatcher(expected, true);
	}
	
	@Override
	public void describeTo(Description description) {
		describeTo(description, expected);
	}
	
	@Override
	protected void describeMismatchSafely(MimeMessage actual, Description description) {
		describeTo(description, actual);
	}

	@Override
	protected boolean matchesSafely(MimeMessage actual) {
		try {
			// TODO: use arrayContaining when we know why recipients are duplicated
			return hasItems(internetAddresses(expected.getAllRecipients())).matches(asList(actual.getAllRecipients()))
				&& arrayContaining(expected.getFrom()).matches(actual.getFrom())
				&& expected.getSubject().equals(actual.getSubject())
				&& contentMatches(expected.getContent(), actual.getContent());
		}
		catch (MessagingException | IOException exception) {
			return false;
		}
	}
	
	private boolean contentMatches(Object expectedContent, Object actualContent) {
		if (matchContentUsingRegEx) {
			return actualContent.toString().matches(expectedContent.toString());
		}
		return expectedContent.equals(actualContent);
	}

	private static void describeTo(Description description, MimeMessage message) {
		try {
			describeRecipientsTo(description, message, RecipientType.TO);
			describeRecipientsTo(description, message, RecipientType.CC);
			describeRecipientsTo(description, message, RecipientType.BCC);
			description.appendText("From: ").appendValue(message.getFrom()).appendText("\n");
			description.appendText("Subject: ").appendValue(message.getSubject()).appendText("\n");
			description.appendText("Content: ").appendValue(message.getContent()).appendText("\n");
		}
		catch (MessagingException | IOException exception) {
			description.appendValue(exception.getMessage());
		}
	}

	private static void describeRecipientsTo(Description description, MimeMessage message, RecipientType type) {
		Object value;
		
		try {
			value = message.getRecipients(type);
		}
		catch (MessagingException exception) {
			value = exception.getMessage();
		}
		
		if (value != null) {
			description.appendText(type.toString()).appendText(": ").appendValue(value).appendText("\n");
		}
	}
}
