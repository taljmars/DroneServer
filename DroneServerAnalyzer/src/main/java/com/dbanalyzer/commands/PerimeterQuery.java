package com.dbanalyzer.commands;

import com.db.persistence.remote_exception.QueryRemoteException;
import com.db.persistence.scheme.BaseObject;
import com.dbanalyzer.QuerySvcRemoteWrapper;
import com.dronedb.persistence.scheme.PolygonPerimeter;
import com.generic_tools.Pair.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class PerimeterQuery implements RunnablePayload {

    @Autowired
    private QuerySvcRemoteWrapper querySvcRemote;

    private List<Pair<String, String>> usage;

    @PostConstruct
    public void init() {
        usage = new ArrayList<>();
        usage.add(new Pair<>("pq","Free query"));
    }

    @Override
    public boolean isRelevant(String payload) {
        if (!payload.startsWith("pq "))
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
//        try {
//            QueryRequestRemote queryRequestRemote = new QueryRequestRemote();
//
//            String queryString = payload.substring("pq ".length());
//            QueryResponseRemote queryResponseRemote = querySvcRemote.runNativeQueryWithClass(queryString, PolygonPerimeter.class.getCanonicalName());

            List<BaseObject> perimetersList = null;// = queryResponseRemote.getResultList();
            ans += "Total Perimeters: " ;//+ perimetersList.size() + "\n";
            ans += String.format("%37s | %11s | %10s | %7s | %7s | %10s | %5s\n",
                    "UUID", "fromVersion", "toVersion", "Deleted", "DB",
                    "Name", "Items"
                    );
            for (String a : Collections.nCopies(37+11+10+7+7+10+5 + 12 + 6, "-")) ans +=a;
            ans += "\n";

            for (BaseObject prm : perimetersList) {
                PolygonPerimeter polygonPerimeter = (PolygonPerimeter) prm;
                List<String> uids = polygonPerimeter.getPoints();
                ans += String.format("%37s | %11d | %10d | %7s | %7s | %10s | %5d\n",
                        polygonPerimeter.getKeyId().getObjId(), polygonPerimeter.getFromRevision(), polygonPerimeter.getKeyId().getToRevision(),
                        polygonPerimeter.isDeleted() ? "T" : "", !polygonPerimeter.getKeyId().getEntityManagerCtx().equals(0) ? "PRIVATE" : "PUBLIC",
                        polygonPerimeter.getName(),
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
