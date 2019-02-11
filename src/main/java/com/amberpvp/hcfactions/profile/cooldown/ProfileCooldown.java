package com.amberpvp.hcfactions.profile.cooldown;

import com.amberpvp.hcfactions.util.DateUtil;
import com.amberpvp.hcfactions.util.DateUtil;
import lombok.Getter;
import org.apache.commons.lang.time.DurationFormatUtils;

import java.text.DecimalFormat;

public class ProfileCooldown {

    private static final DecimalFormat SECONDS_FORMATTER = new DecimalFormat("#0.0");

    @Getter
    private final ProfileCooldownType type;
    private final long duration;
    private final long createdAt;

    public ProfileCooldown(ProfileCooldownType type, long duration) {
        this.type = type;
        this.duration = duration * 1000;
        this.createdAt = System.currentTimeMillis();
    }

    public boolean isFinished() {
        return ((createdAt + duration) - System.currentTimeMillis()) <= 0;
    }

    public String getTimeLeft() {

        long time = (createdAt + duration) - System.currentTimeMillis();
        if (time >= 3600000) {
            return DateUtil.formatTime(time);
        } else if (time >= 60000) {
            return DateUtil.formatTime(time);
        } else {
            return SECONDS_FORMATTER.format(((time) / 1000.0f)) + "s";
        }
    }

    public long getSecondsLeft() {
        long time = (createdAt + duration) - System.currentTimeMillis();
        return (time / 1000);
    }

}
