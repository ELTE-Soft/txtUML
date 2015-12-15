package org.mockito.configuration;

/**
 * This class disables objenesis cache for mockito mocking that caused class
 * cast exceptions when generating mocks.
 */
public class MockitoConfiguration extends DefaultMockitoConfiguration {

	@Override
	public boolean enableClassCache() {
		return false;
	}

}
