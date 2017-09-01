package com.dbanalyzer;

import com.dronedb.persistence.scheme.*;
import com.dronedb.persistence.ws.internal.*;
import com.dronedb.persistence.ws.internal.DatabaseValidationRemoteException;
import com.dronedb.persistence.ws.internal.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class Analyzer {

    private DroneDbCrudSvcRemote droneDbCrudSvcRemote;
    private MissionCrudSvcRemote missionCrudSvcRemote;
    private QuerySvcRemote querySvcRemote;

    private Analyzer() {
        droneDbCrudSvcRemote = AppConfig.context.getBean(DroneDbCrudSvcRemote.class);
        //missionCrudSvcRemote = AppConfig.context.getBean(MissionCrudSvcRemote.class);
        querySvcRemote = AppConfig.context.getBean(QuerySvcRemote.class);
    }

    @Transactional
    public static void main(String[] args) throws com.dronedb.persistence.ws.internal.DatabaseValidationRemoteException, ObjectNotFoundException {
        //System.setProperty("javax.xml.bind.JAXBContext", "com.sun.xml.internal.bind.v2.ContextFactory");

        System.out.println("Start Drone Server Analyzer");

        Analyzer analyzer = new Analyzer();

        byte[] buff = new byte[100];
        Scanner reader = new Scanner(System.in);
        while (reader.hasNext()) {
            String s = reader.next();
            System.out.println("Received '" + s + "' from user");
            switch (s) {
                case "ms":
                    analyzer.showMission();
                    break;
            }
        }
    }

    private void showMission() throws ObjectNotFoundException {
        QueryRequestRemote queryRequestRemote = new QueryRequestRemote();
        queryRequestRemote.setClz(Mission.class.getName());
        queryRequestRemote.setQuery("GetAllMissions");
        QueryResponseRemote queryResponseRemote = querySvcRemote.query(queryRequestRemote);
        List<BaseObject> missionList = queryResponseRemote.getResultList();
        for (BaseObject msn : missionList) {
            Mission mission = (Mission) msn;
            List<String> uids = mission.getMissionItemsUids();
            for (String uuid : uids) {
                MissionItem missionItem = (MissionItem) droneDbCrudSvcRemote.read(uuid);
                System.out.println(missionItem.toString());
            }
        }
    }
}
