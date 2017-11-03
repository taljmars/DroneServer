package com.db.persistence.web.internal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DBObjectizer {

    @Autowired
    private ObjectizeTable query;

    public Map<String, List<List<String>>> run(String payload) {
        List<Class> tables = ShowTables.getTableClass();

        Map<String, List<List<String>>> ans = new HashMap<>();
        for (Class clz : tables) {
            ans.put(clz.getSimpleName() , query.run("select a from " + clz.getSimpleName() + " a"));
        }
        return ans;
    }
}
