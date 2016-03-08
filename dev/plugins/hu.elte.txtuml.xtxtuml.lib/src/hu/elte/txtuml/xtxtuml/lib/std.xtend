package hu.elte.txtuml.xtxtuml.lib;

import hu.elte.txtuml.utils.Logger

public class std {

	public static class out {
		def public static void println(String message) {
			Logger.user.info(message);
		}
	}

}
