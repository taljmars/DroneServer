package com.events;

import com.db.persistence.events.ServerEvent;
import com.db.persistence.events.ServerEventMapper;
import org.reflections.Reflections;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class ServerEventMapperImpl implements ServerEventMapper {

    private Map<Class, String> eventIds;

    @PostConstruct
    public void init() {
        System.out.println("This is sparta");
        Map<Integer, Class> occupiedIds = new HashMap<>();
        eventIds = new HashMap<>();
        Reflections reflections = new Reflections("com.db.persistence.events");
        Set<Class<?>> allClasses = reflections.getTypesAnnotatedWith(ServerEvent.class);
        for (Class c : allClasses) {
            ServerEvent serverEvent = (ServerEvent) c.getAnnotation(ServerEvent.class);
            int id = serverEvent.id();
            if (occupiedIds.get(id) != null) {
                String msg = "Failed to sign event " + c.getSimpleName() + ", ID is already being used by " + occupiedIds.get(id).getSimpleName() + ", (ID=EID" + serverEvent.id();
                System.out.println(msg);
                throw new RuntimeException(msg);
            }
            System.out.println("Event Type: " + c + ", ID=EID" + serverEvent.id());
            eventIds.put(c, String.format("EID%010d", serverEvent.id()));
            occupiedIds.put(id, c);
        }
    }



    @Override
    public String getEventCode(Class<?> aClass) {
        return eventIds.get(aClass);
    }
}
