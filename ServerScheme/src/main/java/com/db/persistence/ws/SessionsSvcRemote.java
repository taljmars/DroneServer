package com.db.persistence.ws;

import org.springframework.web.bind.annotation.*;

/**
 * Created by taljmars on 4/28/17.
 */
public interface SessionsSvcRemote {

    @RequestMapping(value = "/publish", method = RequestMethod.POST)
    void publish();

    @RequestMapping(value = "/discard", method = RequestMethod.POST)
    void discard();

}