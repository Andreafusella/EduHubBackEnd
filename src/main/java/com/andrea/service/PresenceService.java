package com.andrea.service;

import com.andrea.dao.PresenceDao;
import com.andrea.model.Presence;

public class PresenceService {
    private PresenceDao presenceDao = new PresenceDao();

    public boolean addPresence(Presence presence) {
        return presenceDao.addPresence(presence);
    }

    public boolean updatePresence(Presence presence) {
        return presenceDao.updatePresence(presence);
    }
}
