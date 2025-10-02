package domain.group;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

@Builder(toBuilder = true)
@Getter
@EqualsAndHashCode
public class Group {
    private static final Integer DEFAULT_FREQUENCY = 7;
    @NonNull
    private String username;
    @NonNull
    private String name;
    @NonNull
    @Builder.Default
    private Integer frequencyInDays = DEFAULT_FREQUENCY;

    public String toHumanReadableString() {
        return "Name = " + this.name + "\n" +
                "Frequency (In Days) = " + this.frequencyInDays;
    }
}
