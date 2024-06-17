
```
mvn package

alias reconnect="java -cp \"/Users/sharib.jafari/.m2/repository/info/picocli/picocli/4.7.6/picocli-4.7.6.jar:/Users/sharib.jafari/Documents/reconnect/reconnect/target/reconnect-1.0-SNAPSHOT.jar\" application.ShellApplication"
```

```
mvn package appassembler:assemble

sh target/appassembler/bin/reconnect
```