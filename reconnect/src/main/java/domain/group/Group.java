package domain.group;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
@EqualsAndHashCode
public class Group {
    private static final Integer DEFAULT_FREQUENCY = 7;
    @NonNull
    String name;
    @NonNull
    @Builder.Default
    Integer frequencyInDays = DEFAULT_FREQUENCY;

    public String toHumanReadableString() {
        return "Name = " + this.name + "\n" +
                "Frequency (In Days) = " + this.frequencyInDays;
    }
}
