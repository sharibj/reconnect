package domain.group;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
public class Group {
    @NonNull
    String name;
    @NonNull
    Integer frequencyInDays;

    public String toHumanReadableString() {
        return "Name = " + this.name + "\n" +
                "Frequency (In Days) = " + this.frequencyInDays;
    }
}
