package org.example.server.manager;

import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;


@Component
public class DeskStateManager {
    private final Map<String, String> previousStateMap = new HashMap<>();

    public void savePreviousState(String deskId, String state) {
        previousStateMap.put(deskId, state);
    }

    public String getPreviousState(String deskId) {
        return previousStateMap.get(deskId);
    }

    public void clearPreviousState(String deskId) {
        previousStateMap.remove(deskId);
    }
}
