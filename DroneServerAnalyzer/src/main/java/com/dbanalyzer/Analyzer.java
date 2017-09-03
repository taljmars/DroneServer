package com.dbanalyzer;

import com.dronedb.persistence.scheme.*;
import javafx.util.StringConverter;
import javassist.tools.rmi.ObjectNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

public class Analyzer {

    class AnalyzerCmd {
        public String cmd;
        public String desc;
        public Runnable op;
        public AnalyzerCmd(String cmd, String desc, Runnable op) {
            this.cmd = cmd; this.desc = desc; this.op = op;
        }
    }

    private Map<String, Runnable> handlers;
    private Map<String, String> usages;

    private DroneDbCrudSvcRemote droneDbCrudSvcRemote;
    private MissionCrudSvcRemote missionCrudSvcRemote;
    private QuerySvcRemote querySvcRemote;

    private Analyzer() {
        droneDbCrudSvcRemote = AppConfig.context.getBean(DroneDbCrudSvcRemote.class);
        //missionCrudSvcRemote = AppConfig.context.getBean(MissionCrudSvcRemote.class);
        querySvcRemote = AppConfig.context.getBean(QuerySvcRemote.class);

        List<AnalyzerCmd> cmds = new ArrayList<AnalyzerCmd>();
        cmds.add(new AnalyzerCmd("ms", "Show mission in DB", () -> showMission()));
        cmds.add(new AnalyzerCmd("mission", "Show mission in DB", () -> showMission()));

        handlers = new HashMap<>();
        usages = new HashMap<>();
        for (AnalyzerCmd analyzerCmd : cmds) {
            handlers.put(analyzerCmd.cmd, analyzerCmd.op);
            usages.put(analyzerCmd.cmd, analyzerCmd.desc);
        }
    }

    @Transactional
    public static void main(String[] args) throws ObjectNotFoundException {
        System.out.println("Start Drone Server Analyzer");

        Analyzer analyzer = new Analyzer();

        byte[] buff = new byte[100];
        Scanner reader = new Scanner(System.in);
        while (reader.hasNext()) {
            String s = reader.next();
            System.out.println("Received '" + s + "' from user");

            Runnable runnable = analyzer.handlers.get(s);
            if (runnable == null) {
                System.out.println("Command '" + s + "' is not recognized");
                analyzer.usage();
                continue;
            }
            runnable.run();
            System.out.println();
        }
    }

    private void usage() {
        System.out.println("Usage -- DB Analyzer command");
        for (Map.Entry<String, String> entry : usages.entrySet()) {
            System.out.println(String.format("%s20\t%s", entry.getKey(), entry.getValue()));
        }
        System.out.println();
    }

    private void showMission() {
        try {
            QueryRequestRemote queryRequestRemote = new QueryRequestRemote();
            queryRequestRemote.setClz(Mission.class);
            queryRequestRemote.setQuery("GetAllMissions");
            QueryResponseRemote queryResponseRemote = querySvcRemote.query(queryRequestRemote);
            List<BaseObject> missionList = queryResponseRemote.getResultList();
            System.out.println("Total Mission: " + missionList.size() + "\n");
            for (BaseObject msn : missionList) {
                Mission mission = (Mission) msn;
                System.out.println("Mission Name: " + mission.getName());
                System.out.println("Default Alt: " + mission.getDefaultAlt() + "m");
                List<UUID> uids = mission.getMissionItemsUids();
                System.out.println("Mission Items (" + uids.size() + "):");
                for (UUID uuid : uids) {
                    MissionItem missionItem = (MissionItem) droneDbCrudSvcRemote.read(uuid);
                    System.out.println(missionItem.toString());
                }
                System.out.println();
            }
        }
        catch (ObjectNotFoundException e) {

        }
    }
}
