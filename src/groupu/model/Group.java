package groupu.model;

public final class Group {
    private final String name;
    private String description;
    private String admin;
    private String tags;

    public Group(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // how you're supposed to implement equals
    public boolean equals(Object other) {
        if (other == this) return true;
        if (other == null) return false;
        if (other.getClass() != this.getClass()) return false;
        Group that = (Group) other;
        return (this.name.equals(that.name)) && (this.description == that.description);
    }

    public String toString() {
        return name + " " + description;
    }
}
