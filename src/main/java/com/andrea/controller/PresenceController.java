package com.andrea.controller;

import com.andrea.service.PresenceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;
import com.andrea.model.Presence;
import java.util.List;

public class PresenceController {
    private PresenceService presenceService = new PresenceService();
    private ObjectMapper objectMapper = new ObjectMapper();

    public void registerRoutes(Javalin app) {
        app.post("/update-presences", this::updatePresence);

    }

    public void addPresence(Context ctx) {
        try {

            List<Presence> presenceList = objectMapper.readValue(ctx.body(), objectMapper.getTypeFactory().constructCollectionType(List.class, Presence.class));

            for (Presence presence : presenceList) {
                boolean flag = presenceService.addPresence(presence);
                if (!flag) {
                    ctx.status(401).json("Error add presence");
                    return;
                }
            }
            ctx.status(200).json("Presence added");
        } catch (Exception e) {

            ctx.status(400).json("Invalid JSON format");
            e.printStackTrace();
        }
    }

    public void updatePresence(Context ctx) {
        try {

            List<Presence> presenceList = objectMapper.readValue(ctx.body(), objectMapper.getTypeFactory().constructCollectionType(List.class, Presence.class));

            for (Presence presence : presenceList) {
                boolean flag = presenceService.updatePresence(presence);
                if (!flag) {
                    ctx.status(401).json("Error update presence");
                    return;
                }
            }
            ctx.status(200).json("Presence update");
        } catch (Exception e) {

            ctx.status(400).json("Invalid JSON format");
            e.printStackTrace();
        }
    }
}
