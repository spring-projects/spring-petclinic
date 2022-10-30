/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.system;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.SecureRandom;

/**
 * Controller used to showcase what happens when an exception is thrown
 *
 * @author Michael Isvy
 * <p/>
 * Also see how a view that resolves to "error" has been added ("error.html").
 */
@Controller
class CrashController {

	private final SecureRandom rand = new SecureRandom();
	private final ExceptionFlavor exceptionFlavor = new ExceptionFlavor();

	@GetMapping("/oups")
	public String triggerException() {
		throw new RuntimeException(
			"Expected: controller used to showcase what " + "happens when an exception is thrown");
	}

	@GetMapping("/appErr1")
	public String triggerAppException1() {
		triggerSomeError();
		return "appErr1";
	}

	@GetMapping("/appErr2")
	public String triggerAppException2() {
		triggerSomeError();
		return "appErr2";
	}

	protected void triggerSomeError() {
		int randVal = 1 + rand.nextInt(4);
		switch (randVal) {
			case 1:
				throw new AppException("type1: throwing app exception");
				//break;
			case 2:
				throw new AppInnerException("type2: throwing app inner exception");
				//break;
			case 3:
				exceptionFlavor.type3();
				break;
			case 4:
				exceptionFlavor.type4();
				break;
		}
	}

	protected static class ExceptionFlavor {

		protected void type3() {
			throw new AppException("type3: inner class throwing app exception");
		}

		protected void type4() {
			throw new AppInnerException("type4: inner class throwing inner exception");
		}
	}

	protected static class AppInnerException extends RuntimeException {
		public AppInnerException(String message) {
			super(message);
		}
	}

}
