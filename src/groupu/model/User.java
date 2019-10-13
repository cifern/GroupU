package groupu.model;

    public final class User {
        private final String name;
        private final long info;

        public User(String name, long info) {
            this.name = name;
            this.info = info;
        }

        // how you're supposed to implement equals
        public boolean equals(Object other) {
            if (other == this) return true;
            if (other == null) return false;
            if (other.getClass() != this.getClass()) return false;
            User that = (User) other;
            return (this.name.equals(that.name)) && (this.info == that.info);
        }

        public String toString() {
            return name + " " + info;
        }
}
