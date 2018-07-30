package com.dbanalyzer.commands;

import com.db.persistence.remote_exception.QueryRemoteException;
import com.db.persistence.scheme.BaseObject;
import com.db.persistence.scheme.QueryRequestRemote;
import com.db.persistence.scheme.QueryResponseRemote;
import com.dbanalyzer.QuerySvcRemoteWrapper;
import com.dronedb.persistence.scheme.Mission;
import com.generic_tools.Pair.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
public class MissionQuery implements RunnablePayload {

    @Autowired
    private QuerySvcRemoteWrapper querySvcRemote;

    private List<Pair<String, String>> usage;

    @PostConstruct
    public void init() {
        usage = new ArrayList<>();
        usage.add(new Pair<>("mq","Free query"));
    }

    @Override
    public boolean isRelevant(String payload) {
        if (!payload.startsWith("mq "))
            return false;

        String[] strs = payload.split(" ");
        return strs.length > 2;
    }

    @Override
    public List<Pair<String, String>> getUsage() {
        return usage;
    }

    @Override
    public String run(String payload) {
        String ans = "";
//        try {
            QueryRequestRemote queryRequestRemote = new QueryRequestRemote();

            String queryString = payload.substring("mq ".length());
            QueryResponseRemote queryResponseRemote;// = querySvcRemote.runNativeQueryWithClass(queryString, Mission.class.getCanonicalName());
//        QueryResponseRemote queryResponseRemote = querySvcRemote.runNativeQuery(queryString);

            List<BaseObject> missionList = null;//= queryResponseRemote.getResultList();
            ans += "Total Mission: " ;//+ missionList.size() + "\n";
            ans += String.format("%37s | %11s | %10s | %7s | %7s | %10s | %6s | %5s\n",
                    "UUID", "fromVersion", "toVersion", "Deleted", "DB",
                    "Name", "Alt", "Items"
            );
            for (String a : Collections.nCopies(37 + 11 + 10 + 7 + 7 + 10 + 6 + 5 + 14 + 7, "-")) ans += a;
            ans += "\n";

            for (BaseObject msn : missionList) {
                Mission mission = (Mission) msn;
                List<String> uids = mission.getMissionItemsUids();
                ans += String.format("%37s | %11d | %10d | %7s | %7s | %10s | %5dm | %5d\n",
                        mission.getKeyId().getObjId(), mission.getFromRevision(), mission.getKeyId().getToRevision(),
                        mission.isDeleted() ? "T" : "", !mission.getKeyId().getEntityManagerCtx().equals(0) ? "PRIVATE" : "PUBLIC",
                        mission.getName(),
                        (int) mission.getDefaultAlt(),
                        uids.size()
                );
            }
//        }
//        catch (QueryRemoteException e) {
//            ans += "ERROR: " + e.getMessage();
//        }
        ans += "\n";
        return ans;
    }
}
