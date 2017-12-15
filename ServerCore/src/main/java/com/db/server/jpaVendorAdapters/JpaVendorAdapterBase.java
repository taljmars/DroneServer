package com.db.server.jpaVendorAdapters;

import org.springframework.orm.jpa.JpaVendorAdapter;

import java.util.Properties;

public interface JpaVendorAdapterBase extends JpaVendorAdapter {

    Properties getProperties();
}
