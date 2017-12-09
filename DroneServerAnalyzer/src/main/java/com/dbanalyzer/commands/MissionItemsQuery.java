package com.dbanalyzer.commands;

import com.db.persistence.remote_exception.QueryRemoteException;
import com.db.persistence.scheme.BaseObject;
import com.db.persistence.wsSoap.QueryRequestRemote;
import com.db.persistence.wsSoap.QueryResponseRemote;
import com.dbanalyzer.QuerySvcRemoteWrapper;
import com.dronedb.persistence.scheme.Mission;
import com.dronedb.persistence.scheme.MissionItem;
import com.generic_tools.Pair.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Component
public class MissionItemsQuery implements RunnablePayload {

    @Autowired
    private QuerySvcRemoteWrapper querySvcRemote;

    private List<Pair<String, String>> usage;

    @PostConstruct
    public void init() {
        usage = new ArrayList<>();
        usage.add(new Pair<>("miq","Free query"));
    }

    @Override
    public boolean isRelevant(String payload) {
        if (!payload.startsWith("miq "))
            return false;

        String[] strs = payload.split(" ");
        if (strs.length <= 2)
            return false;

        return true;
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

            String queryString = payload.substring("miq ".length());
            QueryResponseRemote queryResponseRemote = querySvcRemote.runNativeQueryWithClass(queryString, MissionItem.class.getCanonicalName());

            List<BaseObject> missionList = queryResponseRemote.getResultList();
            ans += "Total Mission: " + missionList.size() + "\n";
            ans += String.format("%37s | %11s | %10s | %7s | %7s | %10s | %6s | %6s\n",
                    "UUID", "fromVersion", "toVersion", "Deleted", "DB",
                    "Type", "Lat", "Lng"
            );
            for (String a : Collections.nCopies(37 + 11 + 10 + 7 + 7 + 10 + 6 + 6 + 14 + 7, "-")) ans += a;
            ans += "\n";

            for (BaseObject msn : missionList) {
                MissionItem missionItem = (MissionItem) msn;
                ans += String.format("%37s | %11d | %10d | %7s | %7s | %10s | %6dd | %6dd\n",
                        missionItem.getKeyId().getObjId(), missionItem.getFromRevision(), missionItem.getKeyId().getToRevision(),
                        missionItem.isDeleted() ? "T" : "", !missionItem.getKeyId().getEntityManagerCtx().equals(0) ? "PRIVATE" : "PUBLIC",
                        missionItem.getClz().getSimpleName(),
                        missionItem.getLat(),
                        missionItem.getLon()
                );
            }
        }
        catch (QueryRemoteException e) {
            ans += "ERROR: " + e.getMessage();
        }
        ans += "\n";
        return ans;
    }
}
