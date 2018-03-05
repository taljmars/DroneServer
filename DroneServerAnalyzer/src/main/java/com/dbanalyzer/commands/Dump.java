package com.dbanalyzer.commands;

import com.dbanalyzer.QuerySvcRemoteWrapper;
import com.generic_tools.Pair.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
public class Dump implements RunnablePayload {

    @Autowired
    private Query query;

    private List<Pair<String, String>> usage;

    @PostConstruct
    public void init() {
        usage = new ArrayList<>();
        usage.add(new Pair<>("dump","Print all tables in DB"));
        usage.add(new Pair<>("dump -u <UserName>","Print all tables in DB for <UserName>"));
    }

    @Override
    public boolean isRelevant(String payload) {
        for (Pair pair : usage) {
            if (pair.getFirst().equals(payload))
                return true;
            else {
                int idx = pair.getFirst().toString().indexOf("<");
                if (idx <= 0)
                    continue;
                if (payload.contains(pair.getFirst().toString().substring(0, idx - 1))) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public List<Pair<String, String>> getUsage() {
        return usage;
    }

    @Override
    public String run(String payload) {
        String[] strings = payload.split(" ");
        if (strings.length == 3) {
//            QuerySvcRemoteWrapper.userName = strings[2];
//            System.out.println("Dump dedicated user table of '" + QuerySvcRemoteWrapper.userName + "'");
        }
        else {
//            QuerySvcRemoteWrapper.userName = "PUBLIC";
        }
        System.out.println(payload);
        String ans = "Tables:\n";
        List<Class> tables = ShowTables.getTableClass();
        for (Class clz : tables)
            ans += "  " + clz.getSimpleName() + "\n";

        ans += "\nDetails:\n";
        for (Class clz : tables) {
            ans += clz.getSimpleName() + "\n";
            ans += query.run("q select a from " + clz.getSimpleName() + " a") + "\n";
        }

        return ans;
    }
}
