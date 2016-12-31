package org.tpc.resources;

import static org.junit.Assert.*;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

public class PrefixedBundledMessageSourceTest {

	private PrefixedBundledMessageSource mSource;

	@Before
	public void setUp() throws Exception {
		mSource = new PrefixedBundledMessageSource();
		
		// Adds bundle "world.properties" with prefix "hello"
		mSource.addBasenames("hello#world");
	}

	@Test
	public void test() {
		assertEquals("value", mSource.getMessage("hello.test.key", null, Locale.ENGLISH));
	}

}
