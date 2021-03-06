package com.auditdb.persistence;

import com.plugins_manager.PluginManifest;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@ComponentScan({
        "com.auditdb.persistence",
})
@Configuration
public class AuditLogsSchemeManifest implements PluginManifest {

    private final static Logger LOGGER = Logger.getLogger(AuditLogsSchemeManifest.class);

    @PostConstruct
    public void init() {
        LOGGER.debug("Loading AuditLogsSchemeManifest");
    }


    @Override
    public List<String> getExceptionsPackage() {
        List<String> arr = new ArrayList<>();
        return arr;
    }

    @Override
    public List<String> getWebServicePackage() {
        List<String> arr = new ArrayList<>();
        return arr;
    }

    @Override
    public List<String> getSchemePackage() {
        List<String> arr = new ArrayList<>();
        arr.add("com.auditdb.persistence.scheme");
        arr.add("com.auditdb.persistence.base_scheme");
        return arr;
    }

}

