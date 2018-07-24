/*
 * Tal Martsiano
 * Copyright (c) 2018.
 */

package com.db.persistence.cache;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
public class CacheContainerManager {

    private final static Logger LOGGER = Logger.getLogger(CacheContainerManager.class);

    @Autowired private ApplicationContext context;

    private Map<Integer, CacheContainer> cacheContainerMap;

    @PostConstruct
    public void init() {
        LOGGER.debug("Initialize CacheContainerManager");
        cacheContainerMap = new HashMap<>();
    }

    public CacheContainer create(Integer ctx) {
        CacheContainer cacheContainer = context.getBean(CacheContainer.class);
        if (cacheContainerMap.containsKey(ctx)) {
            LOGGER.error("Cache out of sync!, ctx " + ctx + " already exist !!!");
            System.exit(-3);
        }
        cacheContainerMap.put(ctx, cacheContainer);
        return cacheContainer;
    }

    public CacheContainer get(Integer ctx) {
        if (!cacheContainerMap.containsKey(ctx))
            return create(ctx);
        return cacheContainerMap.get(ctx);
    }

    public CacheContainer delete(Integer ctx) {
        return cacheContainerMap.remove(ctx);
    }

    public void dump() {
        System.out.println(toString());
    }

    @Override
    public String toString() {
        String res = "";
        res += "CacheContainer status ( " + cacheContainerMap.keySet().size() + " containers):\n";
        for (Integer ctx : cacheContainerMap.keySet()) {
            res += "Container #" + ctx + "\n";
            CacheContainer container = cacheContainerMap.get(ctx);
            res += container.toString() + "\n";
        }
        return res.substring(0, res.length() - 1);
    }
}
