package domain.group;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
public class Group {
    @Builder.Default
    String id = UUID.randomUUID().toString();
    @NonNull
    String name;
    @NonNull
    Integer frequencyInDays;

    public String toHumanReadableString() {
        return "ID = " + this.id + "\n" +
                "Name = " + this.name + "\n" +
                "Frequency (In Days) = " + this.frequencyInDays;
    }
}
