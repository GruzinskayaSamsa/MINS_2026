package delivery.model;

public class Courier {

    private final int id;
    private final String name;
    private boolean available = true;

    public Courier(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}