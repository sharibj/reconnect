package application;

import java.util.concurrent.Callable;

import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(name = "reconnect", subcommands = { GroupCommand.class }, mixinStandardHelpOptions = true, version = "checksum 4.0",
        description = "Coming Soon")
public class ShellApplication implements Callable<Integer> {


    @Override
    public Integer call() throws Exception {
        println("ShellApplication Called");
        return 0;
    }

    static void println(String line) {
        System.out.println(line);
    }

    public static void main(String[] args) {
        int rc = new CommandLine(new ShellApplication()).execute(args);
        System.exit(rc);
    }
}
