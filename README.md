# User peresentable Logger

Logger library for presenting the results, issues of batch and background jobs, events to the user.

This project is built with a 'managment' type application in mind where users have to handle multiple types of processes in a way that a dashboard with ongoing jobs, unresolved issues, unexpected or expected events is neccessary.

## How-to

Create logging presets

```java

  var logger = new Logger<MyRole, MyLevel>();

  // log preset with 3 variables, with ADMIN mask on it's last one
  var login = logger.preset(MyRole.USER, MyLevel.INFO, "login")
    .m("{} - login user: {} with password ").v(Data.of(MyRole.ADMIN))
    .build();

  // use log preset
  login.log(System.currentTimeMillis(), "username", "password");

```

Create a shothand logger class

```java
public class UserAuthenticationLogger {

  public static final Logger<MyRole, MyLevel> logger = new Logger<>();

  private static final String loggedIn = "{}: user '{}' logged in.";
  private static final String loggerOut = "{}: user '{}' logged out.";
  private static final String unsuccessfulAuthentication = "{}: user '{}' tried to sign but gave bad password: '{}'.";

  public void loggedIn (String username) {
    logger.log(MyLevel.INFO, loggedIn, System.currentTimeMillis(), username);
  }

  public void loggerOut (String username) {
    logger.log(MyLevel.INFO, loggerOut, System.currentTimeMillis(), username);
  }

  public void unsuccessfulAuthentication (String username, String badPass) {
    logger.log(MyLevel.INFO, unsuccessfulAuthentication, System.currentTimeMillis(), username, Data.of(badPass, MyRole.ADMIN));
  }
}
```

Querying logs

```java

  var logger = new Logger<MyRole, MyLevel>();

  // ... logging

  logger.read(MyLevel.INFO, MyRole.USER);
  // List<String> - List of logs, filtered to level INFO or above,
  // with all of their variables masked to fit USER

  logger.readGroups(MyLevel.INFO, MyRole.USER);
  // List<String> - Same as .read but it's grouped by log-groups

```

There's a bunch of extra example code in the [example application](/example-app//src/main/java/dev/bh/sp/logger/logger/LoggerApplication.java).

## Known Issues / Backlog

- This library is not yet published as a maven module, that is because imo it is not mature enough, it needs some great refactoring, features and testing setup to reach that point.

- Testing

- When using `Logger` class, you have to specify a `Role` (role) for authority and a `Level` (log level) as generic arguments, meanwhile `Level` has great development support whith autocomplete, `Role` does not have the same.
  We'd like to have this greater support for `Role` too.

- `Preset` class needs a cleanup, currently it seems like it supports way too many kinds of configurations.

- Currently log messages are being grouped by message without it's variables, we should be able to configure a group-message too which is presentable when the group is collapsed

- Example Application is technicly not an application, either it should be made into an applciation or the documentation should stop refering to it as an application.

- Insertion order should be maintained.

- Should have solution for persisting data
