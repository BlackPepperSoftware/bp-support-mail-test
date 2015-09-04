package uk.co.blackpepper.support.mail.test;

import javax.mail.internet.MimeMessage;

import org.junit.rules.ExternalResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Supplier;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import com.icegreen.greenmail.util.ServerSetupTest;

import static org.junit.Assert.fail;

import static com.google.common.base.Preconditions.checkNotNull;

@Component
public class EmailRule extends ExternalResource implements Supplier<MimeMessage> {
	
	private final ServerSetup config;
	
	private GreenMail mailServer;
	
	private int mailIndex;
	
	public EmailRule() {
		this(ServerSetupTest.SMTP);
	}

	@Autowired(required = false)
	public EmailRule(ServerSetup config) {
		this.config = checkNotNull(config, "config");
	}

	@Override
	protected void before() {
		mailServer = new GreenMail(config);
		mailServer.start();
		
		mailIndex = 0;
	}
	
	@Override
	protected void after() {
		mailServer.stop();
	}

	public GreenMail getMailServer() {
		return mailServer;
	}
	
	public MimeMessage waitAndGet() throws InterruptedException {
		if (!mailServer.waitForIncomingEmail(mailIndex + 1)) {
			fail("expected 1 email");
		}
		
		MimeMessage mail = mailServer.getReceivedMessages()[mailIndex];
		mailIndex++;
		
		return mail;
	}

	@Override
	public MimeMessage get() {
		try {
			return waitAndGet();
		}
		catch (InterruptedException exception) {
			throw new IllegalStateException(exception);
		}
	}
}
