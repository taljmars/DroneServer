package com.db.server.JpaVendorAdapters;

import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.stereotype.Component;

import java.util.Properties;

public interface JpaVendorAdapterBase extends JpaVendorAdapter {

    Properties getProperties();
}
