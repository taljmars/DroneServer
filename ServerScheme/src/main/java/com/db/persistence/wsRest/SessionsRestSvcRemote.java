package com.db.persistence.wsRest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by taljmars on 4/28/17.
 */
public interface SessionsRestSvcRemote {

    @RequestMapping(value = "/publish")
    void publish();

    @RequestMapping(value = "/discard")
    void discard();

}