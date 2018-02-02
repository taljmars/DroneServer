package com.db.persistence.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

public interface DashboardSvcRemote {

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    ModelAndView index();
}
