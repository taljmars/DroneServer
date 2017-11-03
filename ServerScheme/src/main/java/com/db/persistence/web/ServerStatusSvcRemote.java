package com.db.persistence.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

public interface ServerStatusSvcRemote {

    @RequestMapping(value = "/status", method = RequestMethod.GET)
    ModelAndView status();

    @RequestMapping(value = "/status2", method = RequestMethod.GET)
	String status2();
}
