# Practical 14 JUnit 5 tests

The project compiles without external dependencies. To run JUnit 5 tests, add JUnit Jupiter to your IDE/build configuration. Suggested test cases:

1. A deposit increases the balance by the deposited amount.
2. Withdrawing more than the permitted balance throws a banking exception.
3. Concurrent deposits produce the exact expected balance because account balance methods are synchronized.

Keep tests in `src/test/java`; `build.bat` and `build.sh` compile the application source only.
