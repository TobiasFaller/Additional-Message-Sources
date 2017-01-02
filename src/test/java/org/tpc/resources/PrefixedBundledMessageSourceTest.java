package org.tpc.resources;

import static org.junit.Assert.*;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.NoSuchMessageException;

public class PrefixedBundledMessageSourceTest {

	private PrefixedBundledMessageSource mSource;

	@Before
	public void setUp() throws Exception {
		mSource = new PrefixedBundledMessageSource();

		mSource.addBasenames("hello#world");  // "world.properties" with prefix "hello"
		mSource.addBasenames("msg#messages/mymessages");  // "mymessages.properties" with "msg" prefix

		mSource.addBasenames("other");  // "other.properties" without prefix
		mSource.addBasenames("another");  // "another.properties" without prefix
	}

	@Test
	public void test() {
		// Prefixed bundles
		assertEquals("value", mSource.getMessage("hello.test.key", null, Locale.ENGLISH));
		assertEquals("_value_", mSource.getMessage("msg.this_is_a_test", null, Locale.ENGLISH));
		
		// Default bundles
		assertEquals("key", mSource.getMessage("another", null, Locale.ENGLISH));
		assertEquals("yav", mSource.getMessage("yak", null, Locale.ENGLISH));
	}
	
	@Test(expected = NoSuchMessageException.class)
	public void testMissingResource() {
		mSource.getMessage("non-existing.key", null, Locale.ENGLISH);
	}

}
