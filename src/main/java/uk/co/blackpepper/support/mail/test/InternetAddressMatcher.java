package uk.co.blackpepper.support.mail.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.mail.Address;
import javax.mail.internet.InternetAddress;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static com.google.common.base.Preconditions.checkArgument;

public class InternetAddressMatcher extends TypeSafeMatcher<Address> {

	private final InternetAddress expected;

	public InternetAddressMatcher(Address expected) {
		checkArgument(expected instanceof InternetAddress, "Can only match InternetAddress, got: %s", expected);
		this.expected = (InternetAddress) expected;
	}
	
	public static Matcher<Address> internetAddress(Address expected) {
		return new InternetAddressMatcher(expected);
	}
	
	public static Matcher<Address>[] internetAddresses(Address... expecteds) {
		List<Matcher<Address>> matchers = new ArrayList<>();
		
		for (Address expected : expecteds) {
			matchers.add(internetAddress(expected));
		}
		
		return matchers.toArray(new Matcher[0]);
	}
	
	@Override
	public void describeTo(Description description) {
		description.appendValue(expected);
	}

	@Override
	protected boolean matchesSafely(Address actual) {
		if (!(actual instanceof InternetAddress)) {
			return false;
		}
		
		InternetAddress actualInternetAddress = (InternetAddress) actual;
		
		return Objects.equals(expected.getPersonal(), actualInternetAddress.getPersonal())
			&& expected.getAddress().equals(actualInternetAddress.getAddress());
	}
}
