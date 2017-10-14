package com.dbanalyzer.commands;

import com.db.persistence.scheme.BaseObject;
import com.dbanalyzer.QuerySvcRemoteWrapper;
import com.generic_tools.Pair.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.Table;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
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
