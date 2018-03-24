package com.auditdb.persistence.dumper;

import com.db.persistence.events.ServerEvent;
import com.auditdb.persistence.base_scheme.EventLogObject;
import com.db.persistence.events.ServerEventMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.stream.Stream;

@Component
public class EventQueue {

    @Autowired
    private ServerEventMapper serverEventMapper;

    private Queue<EventLogObject> eventQueue;

    public EventQueue() {
        eventQueue = new ArrayBlockingQueue<EventLogObject>(100);
    }

    public void queue(Object event, EventLogObject eventForStoring) {
        if (event.getClass().getAnnotation(ServerEvent.class) == null)
            throw new RuntimeException("Event without annotation: " + event.getClass().getName());

        eventForStoring.setEventTime(new Date());
        eventForStoring.setEventCode(serverEventMapper.getEventCode(event.getClass()));
        System.out.println("Will store: " + eventForStoring.toString());
        eventQueue.add(eventForStoring);
    }

    public EventLogObject pop() {
        return eventQueue.remove();
    }

    public Integer size() { return eventQueue.size(); }

    public Stream<? extends EventLogObject> stream() { return eventQueue.stream(); }
}
