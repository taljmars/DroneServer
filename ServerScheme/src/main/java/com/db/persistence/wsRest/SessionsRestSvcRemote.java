package com.db.persistence.wsRest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by taljmars on 4/28/17.
 */
public interface SessionsRestSvcRemote {

    @RequestMapping(value = "/publish")
    void publish();

    @RequestMapping(value = "/publishForUser", method = RequestMethod.POST)
    void publishForUser(@RequestParam String userName);

    @RequestMapping(value = "/discard")
    void discard();

    @RequestMapping(value = "/discardForUser", method = RequestMethod.POST)
    void discardForUser(@RequestParam String userName);

}