package com.amberpvp.hcfactions.profile.kit;

import com.amberpvp.hcfactions.util.DateUtil;

import java.text.DecimalFormat;

public class ProfileKitCooldown {

    private static final DecimalFormat SECONDS_FORMATTER = new DecimalFormat("#0.0");

    private final long duration;
    private final long createdAt;

    public ProfileKitCooldown(long duration) {
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

}
