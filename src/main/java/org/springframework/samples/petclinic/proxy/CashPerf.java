package org.springframework.samples.petclinic.proxy;

import org.springframework.util.StopWatch;

public class CashPerf implements Payment {
	Payment cash = new Cash();

	@Override
	public void pay(int amount) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		cash.pay(amount);

		stopWatch.stop();
		System.out.println(stopWatch.prettyPrint());
	}
}
