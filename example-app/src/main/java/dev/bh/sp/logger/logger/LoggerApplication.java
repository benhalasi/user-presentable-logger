package dev.bh.sp.logger.logger;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import dev.bh.sp.logger.logger.logging.DummyRole;
import dev.bh.sp.logger.logger.logging.ExampleLevel;

@SpringBootApplication
public class LoggerApplication {

	public static void main(String[] args) {
		// SpringApplication.run(LoggerApplication.class, args);

    var sc = new SomeClass();
    var logger = sc.getLogger();

    for (var level : ExampleLevel.values()) {
      sc.getLogger().getLevels().add(level);
    }

    logger.log(DummyRole.USER, ExampleLevel.TRACE, "{}", "this is a variable");
    logger.log(DummyRole.USER, ExampleLevel.TRACE, "{}", Data.of("this is a confidential variable", DummyRole.ADMIN));
    logger.log(DummyRole.USER, ExampleLevel.TRACE, "{}, that starts this log message", "this is a variable");
    logger.log(DummyRole.USER, ExampleLevel.TRACE, "{}, that starts this log message", Data.of("this is a confidential variable", DummyRole.ADMIN));
    logger.log(DummyRole.USER, ExampleLevel.TRACE, "this message ends with: {}", "this is a variable");
    logger.log(DummyRole.USER, ExampleLevel.TRACE, "this message ends with: {}", Data.of("this is a confidential variable", DummyRole.ADMIN));

    var login = logger.preset(DummyRole.USER, ExampleLevel.INFO, "login").m("{} - login user: {} with password ").v(Data.of(DummyRole.ADMIN)).build();
    var logout = logger.preset(DummyRole.USER, ExampleLevel.INFO, "logout").m("{} - logout user: {}").build();
    var badcreds = logger.preset(DummyRole.ADMIN, ExampleLevel.DEBUG, "badcreds").m("{} - someone tried to login with username: {} and password: ").v(Data.of(DummyRole.ADMIN)).build();
    var userdataok = logger.preset(DummyRole.USER, ExampleLevel.WARN, "userdataok").m("{} - processed user data (email: {}, tel: {}) at line {} ").build();
    var userdatabad = logger.preset(DummyRole.USER, ExampleLevel.WARN, "userdatabad").m("{} - we couldn't process user data (email: ").v(Data.of(DummyRole.ADMIN)).m(", tel: ").v(Data.of(DummyRole.ADMIN)).m(") at line {} due to it's not having xz neccessary data.").build();
    var load = logger.preset(DummyRole.ADMIN, ExampleLevel.INFO, "load").m("{} - current load is {}").build();

    badcreds.log(System.currentTimeMillis(), "useranme", "password");
    badcreds.log(System.currentTimeMillis(), "username", "pasdword");
    login.log(System.currentTimeMillis(), "username", "password");

    for (int i = 0; i < 50; i++) {
      if ( i % 10 == 0) {
        load.log(System.currentTimeMillis(), Math.random());
      }
      if ( i % 6 == 0 || i % 11 == 0 || i % 15 == 0){
        userdatabad.log(System.currentTimeMillis(), "email@example.com", "+99 123 4567", i);
      } else {
        userdataok.log(System.currentTimeMillis(), "email@example.com", "+99 123 4567", i);
      }
    }

    logout.log(System.currentTimeMillis(), "username");

    System.out.println("\n\nuser\n\n");

    for (String line : sc.getLogger().read(ExampleLevel.INFO, DummyRole.USER)) {
      System.out.println(" --- collapsible --- ");
      System.out.println(line);
    }

    System.out.println("\n\nadmin\n\n");

    for (String line : sc.getLogger().read(ExampleLevel.INFO, DummyRole.ADMIN)) {
      System.out.println(" --- collapsible --- ");
      System.out.println(line);
    }

    System.out.println("\n\n" + "uesr" + "\n\n");

    for (String line : sc.getLogger().readGroups(ExampleLevel.TRACE, DummyRole.USER)) {
      System.out.println(" --- collapsible --- ");
      System.out.println(line);
    }

    System.out.println("\n\n" + "admin" + "\n\n");

    for (String line : sc.getLogger().readGroups(ExampleLevel.TRACE, DummyRole.ADMIN)) {
      System.out.println(" --- collapsible --- ");
      System.out.println(line);
    }

	}

}
