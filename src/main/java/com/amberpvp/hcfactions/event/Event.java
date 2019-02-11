package com.amberpvp.hcfactions.event;

import java.util.List;

public interface Event {

    String getName();
    List<String> getScoreboardText();
    boolean isActive();

}
