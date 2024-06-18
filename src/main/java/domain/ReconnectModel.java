package domain;

import java.util.Calendar;
import java.util.Date;

import lombok.Getter;

@Getter
public class ReconnectModel {
    private final String contact;
    private final String group;
    private final Integer frequencyInDays;
    private final Long lastContactedTimestamp;
    private final Long nextContactTimestamp;

    public ReconnectModel(final String contact, final String group, final Integer frequencyInDays, final Long lastContactedTimestamp) {
        this.contact = contact;
        this.group = group;
        this.frequencyInDays = frequencyInDays;
        this.lastContactedTimestamp = lastContactedTimestamp;
        this.nextContactTimestamp = calculateNextContactTimestamp();
    }

    private Long calculateNextContactTimestamp() {
        Calendar calendar = Calendar.getInstance();
        if (lastContactedTimestamp > 0) {
            Date interactionDate = new Date(lastContactedTimestamp);
            calendar.setTime(interactionDate);
            calendar.add(Calendar.DATE, frequencyInDays);
        }
        return calendar.getTime().getTime();
    }

    public boolean isOutOfTouch() {
        long currentTimeStamp = Calendar.getInstance().getTime().getTime();
        return currentTimeStamp >= nextContactTimestamp;
    }

    public String toHumanReadableString() {
        String lastContactedTimestampString = this.lastContactedTimestamp == 0 ? "NEVER" : new Date(this.lastContactedTimestamp).toString();
        return "Name = " + this.contact + "\n" +
                "Group = " + this.group + "\n" +
                "Frequency = " + this.frequencyInDays + "\n" +
                "Last Contacted = " + lastContactedTimestampString + "\n" +
                "Next Time to Contact = " + new Date(this.nextContactTimestamp);
    }
}
