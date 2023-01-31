build: trial rise redemption

run_trial:
	java TrialOfTheOracle

run_rise:
	java RiseOfTheFalseProphet

run_redemption:
	java Redemption

trial: TrialOfTheOracle.java Task.java
	javac $^

rise: RiseOfTheFalseProphet.java Task.java
	javac $^

redemption: Redemption.java Task.java
	javac $^

clean:
	rm -f *.class

.PHONY: build clean
