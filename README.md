# Reconnect

This application helps you stay connected with your contacts by grouping them and letting you track your interactions.

**Contact**: A contact is a person you would like to stay in touch with. Each contact belongs to a group.

**Group** : A group can be something like "family" or "friends". This is where you configure the frequency (e.g. 7 days,
30 days etc.) of getting back in touch.

**Interaction**: An interaction is simply a conversation you had had with your contacts. It can be anything from a text,
call or in-person meeting.

## How does it help?

_Reconnect_ keeps a track of all your interactions and notifies you if you are falling out of touch with any of your
contacts. It's also a great place to log your interactions with simple notes to help you remember your past discussion.

## Where is the APP?

Currently, the application only supports a Command Line Interface (CLI) with all your data stored locally in CSV files.
However, the application is architected to support a number of UI and storage options. A proper app will be implemented
in the future.

## Using the CLI application

### Commands to package jars and run the application manually

```
mvn package

java -cp \"/Users/sharib.jafari/.m2/repository/info/picocli/picocli/4.7.6/picocli-4.7.6.jar:/Users/sharib.jafari/Documents/reconnect/reconnect/target/reconnect-1.0-SNAPSHOT.jar\" application.ShellApplication
```

### Commands to assemble and install application

```
mvn package appassembler:assemble &&
cd target &&
sh installReconnect.sh ~/Document/reconnect_files &&
source ~/.bashrc &&
cd ..
```