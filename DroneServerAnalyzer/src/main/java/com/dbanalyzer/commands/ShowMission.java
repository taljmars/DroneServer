package com.dbanalyzer.commands;

import com.db.persistence.remote_exception.QueryRemoteException;
import com.db.persistence.scheme.BaseObject;
import com.db.persistence.wsSoap.QueryRequestRemote;
import com.db.persistence.wsSoap.QueryResponseRemote;
import com.dbanalyzer.QuerySvcRemoteWrapper;
import com.dronedb.persistence.scheme.Mission;
import com.generic_tools.Pair.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
public class ShowMission implements RunnablePayload {

    @Autowired
    private QuerySvcRemoteWrapper querySvcRemote;

    private List<Pair<String, String>> usage;

    @PostConstruct
    public void init() {
        usage = new ArrayList<>();
        usage.add(new Pair<>("ms","Show mission in DB"));
        usage.add(new Pair<>("mission","Show mission in DB"));
    }

    @Override
    public boolean isRelevant(String payload) {
        for (Pair pair : usage) {
            if (pair.getFirst().equals(payload))
                return true;
        }
        return false;
    }

    @Override
    public List<Pair<String, String>> getUsage() {
        return usage;
    }

    @Override
    public String run(String payload) {
        String ans = "";
        try {
            QueryRequestRemote queryRequestRemote = new QueryRequestRemote();
            queryRequestRemote.setClz(Mission.class.getCanonicalName());
            queryRequestRemote.setQuery("GetAllMissions");
            QueryResponseRemote queryResponseRemote = querySvcRemote.query(queryRequestRemote);
            List<BaseObject> missionList = queryResponseRemote.getResultList();
            ans += "Total Mission: " + missionList.size() + "\n";
            for (BaseObject msn : missionList) {
                Mission mission = (Mission) msn;
                ans += "Mission Name: " + mission.getName() + "\n";
                ans += "Default Alt: " + mission.getDefaultAlt() + "m" + "\n";
                List<String> uids = mission.getMissionItemsUids();
                ans += "Mission Items (" + uids.size() + "):" + "\n";
                for (String uuid : uids) {
//                ResponseEntity<MissionItem> missionItemResponseEntity = objectCrudSvcRemote.read(uuid);
//                MissionItem missionItem = missionItemResponseEntity.getBody();
//                System.out.println(missionItem.toString());
                }
                ans += "\n";
            }
        }
        catch (QueryRemoteException e) {
            ans += "ERROR: " + e.getMessage() + "\n";
        }
        return ans;
    }
}
