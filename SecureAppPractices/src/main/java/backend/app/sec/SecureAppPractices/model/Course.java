package backend.app.sec.SecureAppPractices.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
public class Course {
    private final UUID id;

    @NotNull
    @NotBlank
    private final String name;

    public Course(
            @JsonProperty("id") UUID id,
            @JsonProperty("name") String name
    ) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
