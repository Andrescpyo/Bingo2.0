package Patterns.Behavioral.Command;

public class ExitCommand implements Command {

    @Override
    public void execute() {
        System.exit(0);
    }
}
