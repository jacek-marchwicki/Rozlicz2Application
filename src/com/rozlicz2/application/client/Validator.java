package com.rozlicz2.application.client;

import com.rozlicz2.application.client.resources.ErrorMessages;

public class Validator {
	public static class ValidatorException extends Exception {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private final String message;

		public ValidatorException(String message) {
			this.message = message;
		}

		public String getMessage() {
			return message;
		}
	}
	public static void isNotToShort(String value) throws ValidatorException {
		if (value == null || value.length() < 2)
			throw new ValidatorException(ErrorMessages.messages.toShort(value));
	}
}
