/*
 * Copyright 2014 Black Pepper Software
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
