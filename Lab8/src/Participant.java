import java.io.Serializable;

public class Participant implements Serializable {
    private String name;
    private String email;
    // Add other fields as necessary

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Add other getters and setters
}
