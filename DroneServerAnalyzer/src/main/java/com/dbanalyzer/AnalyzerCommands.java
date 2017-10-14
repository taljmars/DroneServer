package com.dbanalyzer;

import com.dbanalyzer.commands.*;
import com.generic_tools.Pair.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
public class AnalyzerCommands extends ArrayList<RunnablePayload> {

    @Autowired private ShowMission showMission;
    @Autowired private MissionQuery missionQuery;
    @Autowired private MissionItemsQuery missionItemsQuery;
    @Autowired private PerimeterQuery perimeterQuery;
    @Autowired private ShowTables showTables;
    @Autowired private Query query;
    @Autowired private Dump dump;

    @PostConstruct
    public void init() {
        add(dump);
        add(query);
        add(missionItemsQuery);
        add(showTables);
        add(showMission);
        add(perimeterQuery);
        add(missionQuery);
    }

    public String usage() {
        String usage = "";
        usage += "Usage -- DB Analyzer command\n";
        Iterator<RunnablePayload> it = iterator();
        while (it.hasNext()) {
            RunnablePayload runnablePayload = it.next();
            Iterator<Pair<String, String>> t = runnablePayload.getUsage().iterator();
            while (t.hasNext()) {
                Pair<String, String> pair = t.next();
                usage += String.format("  %s\t%s\n", pair.getFirst(), pair.getSecond());
            }
        }
        usage+="\n";

        return usage;
    }

    public RunnablePayload get(String payload) {
        Iterator<RunnablePayload> it = iterator();
        while (it.hasNext()) {
            RunnablePayload runnablePayload = it.next();
            if (runnablePayload.isRelevant(payload))
                return runnablePayload;
        }
        return null;
    }
}
