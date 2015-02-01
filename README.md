Gameserver Service Library
===================

About
-----
This is a convenient helper library for accessing the API of my Gameserver Service project. With this library, you can create and update a server on the server list, while also being able to pull in all servers on the server list. Hence, this library is suitable for both the server and client.

Dependencies
----

**See the Laravel project**: https://github.com/NickToony/gameserver-service

You need a web server running the above project for this to be useful.

**Other Dependencies (which are automatically included by Gradle)**
- OkHTTP (http://square.github.io/okhttp/)
- Gson (https://code.google.com/p/google-gson/)

Installation
------------
1. Clone the repository into your project
2. Add the folder as a module for your project. Then add it to your dependencies
    - e.g. for gradle (replace module name)
`compile project(':module_name')`

Configuration Example
------------
The first thing you **must** do before using any of the methods is set a game configuration. This means that you must create a class that extends the GameserverConfig. For example:

```java
import com.nicktoony.service.GameserverConfig;

/**
 * Created by Nick on 31/01/2015.
 */
public class GameConfig extends GameserverConfig {

    @Override
    public String getServerUrl() {
        return "<server_url_with_/>";
    }

    @Override
    public String getGameAPIKey() {
        return "<game_api_key>";
    }

    @Override
    public void debugLog(String message) {
        System.out.println("GameserverServiceLog :: " + message);
    }

    @Override
    public long getUpdateRate() {
        return 1 * 60 * 1000;
    }

    @Override
    public long getChangedUpdateRate() {
        return 1 * 60 * 1000;
    }
}
```

Ensure the first line of code before calling any methods is

```java
GameserverConfig.setConfig(new GameConfig());
```

Now you can freely use the commands provided.

Server Example
---------------
```java
Host host = new Host("My new server 1", 0, 16);
```
will create and update a server with the name "My new server 1", with 0 / 16 players. You can use the setters on the Host object to change the server name, player count and more.

Client Example
---------------
Create a client object, and call refresh on it. It's that simple. This will fetch all game servers from the list, and handle pagination for you.
```java
Client client = new Client();
client.refresh();
```

Planned Features
---------------
- META data handling
- Filtering by field
- A smarter "quick-refresh"
