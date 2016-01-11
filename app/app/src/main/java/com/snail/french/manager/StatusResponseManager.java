package com.snail.french.manager;


import com.snail.french.constant.FrenchKind;
import com.snail.french.model.status.StatusResponse;

import java.util.HashMap;

/**
 * Created by litingchang on 16-1-12.
 */
public class StatusResponseManager {

    public HashMap<FrenchKind, StatusResponse> statusResponseHashMap;

    private static StatusResponseManager managerInstance;

    private StatusResponseManager() {
        statusResponseHashMap = new HashMap<>();
    }

    public static StatusResponseManager getInstance() {
        if (managerInstance == null) {
            managerInstance = new StatusResponseManager();
        }

        return managerInstance;
    }

    public void put(FrenchKind frenchKind, StatusResponse statusResponse) {
        statusResponseHashMap.put(frenchKind, statusResponse);
    }

    public StatusResponse get(FrenchKind frenchKind) {
        return  statusResponseHashMap.get(frenchKind);
    }

}
