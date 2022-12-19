package dev.bh.sp.logger.logger;

import dev.bh.sp.logger.logger.logging.DummyRole;
import dev.bh.sp.logger.logger.logging.ExampleLevel;

public class SomeClass {

  public static final Logger<DummyRole, ExampleLevel> logger = new Logger<>();

  private static final String login = "{}: user '{}' logged in."; 
  private static final String logout = "{}: user '{}' logged out."; 
  private static final String badcreds = "{}: user '{}' tried to sign but gave bad password: '{}'."; 

  public void login (String username) {
    logger.log(ExampleLevel.INFO, login, System.currentTimeMillis(), username);
  }

  public void logout (String username) {
    logger.log(ExampleLevel.INFO, logout, System.currentTimeMillis(), username);
  }

  public void badcreds (String username, String badPass) {
    logger.log(ExampleLevel.INFO, badcreds, System.currentTimeMillis(), username, Data.of(badPass, DummyRole.ADMIN));
  }

  public Logger<DummyRole, ExampleLevel> getLogger () {
    return logger;
  }

}
