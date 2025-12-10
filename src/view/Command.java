package view;

public abstract class Command {

    private final String key, description;

    public Command(String k, String d) {
        key = k;
        description = d;
    }

    public String getKey() {
        return key;
    }

    public String getDescription() {
        return description;
    }

    public abstract void execute();

}
