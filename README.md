Expense Tracker

This project is configured to target Java 21 (LTS).

What changed:

- pom.xml: set <maven.compiler.release>21</maven.compiler.release> and added the maven-compiler-plugin configuration.
- Added maven-enforcer-plugin rule to require Java >= 21.

How to run locally (macOS):

1. Install JDK 21 (if you want to use exactly Java 21):

   - Homebrew (if available):
     brew install openjdk@21
   - Or download from AdoptOpenJDK / Eclipse Temurin and install.

2. Configure your shell to use JDK 21 (example for zsh):

   - Find the installed path (Homebrew example):
     /opt/homebrew/opt/openjdk@21/libexec/openjdk.jdk/Contents/Home
   - Add to your ~/.zshrc:
     export JAVA_HOME="/opt/homebrew/opt/openjdk@21/libexec/openjdk.jdk/Contents/Home"
     export PATH="$JAVA_HOME/bin:$PATH"

3. Verify:
   mvn -v
   java -version

4. Build:
   mvn clean package

Notes:

- If your machine uses a newer JDK (e.g., Java 25) Maven will still compile with <release>21 when supported by the compiler, but runtime behavior may vary. The enforcer plugin requires Java >= 21 to run the build.
- I attempted to run the automated Java upgrade tool, but the hosted upgrade tool wasn't available for this account; changes were applied manually to the Maven configuration.

If you'd like, I can:

- Try to pin your CI to run on a specific Java 21 JDK image.
- Produce a small test or verify runtime under a JDK 21 docker image.
