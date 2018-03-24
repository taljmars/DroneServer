package com.events;

import com.db.persistence.events.ServerEvent;
import com.db.persistence.events.ServerEventMapper;
import org.apache.log4j.Logger;
import org.reflections.Reflections;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class ServerEventMapperImpl implements ServerEventMapper {

    private final static Logger LOGGER = Logger.getLogger(ServerEventMapperImpl.class);

    private Map<Class, String> eventIds;

    @PostConstruct
    public void init() {
        Map<Integer, Class> occupiedIds = new HashMap<>();
        eventIds = new HashMap<>();
        Reflections reflections = new Reflections("com.db.persistence.events");
        Set<Class<?>> allClasses = reflections.getTypesAnnotatedWith(ServerEvent.class);
        for (Class c : allClasses) {
            ServerEvent serverEvent = (ServerEvent) c.getAnnotation(ServerEvent.class);
            int id = serverEvent.id();
            if (occupiedIds.get(id) != null) {
                LOGGER.error("Failed to sign event " + c.getSimpleName() + ", ID is already being used by " + occupiedIds.get(id).getSimpleName() + ", (ID=EID" + serverEvent.id());
                System.exit(-1);
            }
            LOGGER.debug("Event Type: " + c + ", ID=EID" + serverEvent.id());
            eventIds.put(c, String.format("EID%010d", serverEvent.id()));
            occupiedIds.put(id, c);
        }
    }

    @Override
    public String getEventCode(Class<?> aClass) {
        return eventIds.get(aClass);
    }
}
