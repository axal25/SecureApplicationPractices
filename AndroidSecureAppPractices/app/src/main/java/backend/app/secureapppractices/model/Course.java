package backend.app.secureapppractices.model;

import com.google.gson.Gson;

import java.util.UUID;

public class Course {
    private final UUID id;
    private final String name;

    public Course(
            UUID id,
            String name
    ) {
        this.id = id;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
