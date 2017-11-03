package com.db.persistence.web.internal;

import com.db.persistence.web.ServerStatusSvcRemote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Controller
public class ServerStatusSvcRemoteImpl implements ServerStatusSvcRemote {

    @Autowired
    private DBObjectizer dump;

    @RequestMapping(value = "/status", method = RequestMethod.GET)
    public ModelAndView status() {
        ModelAndView model = new ModelAndView();
//        String res = dump.run("");
        Map<String, List<List<String>>> res = dump.run("");
        model.addObject("title", "talma");
        model.addObject("status", res);
        model.setViewName("status");
        return model;
    }

    int i = 0;
//    @Override
//    public String status(Model model, String table) {
//        String res = dump.run("");
//        model.addAttribute("status", res);
//        return "status";
//    }

    @Override
    public String status2() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Status2: " + i++;
    }
}
