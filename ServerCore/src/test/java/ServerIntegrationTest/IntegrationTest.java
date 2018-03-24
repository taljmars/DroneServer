package ServerIntegrationTest;

import com.db.persistence.services.LoginSvc;
import com.db.persistence.services.ObjectCrudSvc;
import com.db.persistence.services.QuerySvc;
import com.db.server.ServerMain;
import com.generic_tools.validations.RuntimeValidator;
import com.plugins_manager.Plugins;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Iterator;

import static com.db.persistence.workSession.Constant.INTERNAL_SERVER_USER_TOKEN;

//@RunWith(SpringJUnit4ClassRunner.class)
@RunWith(SpringRunner.class)
//@ActiveProfiles({H2, Hibernate})
@ContextConfiguration(classes = ServerMain.class)
public class IntegrationTest {

    static {
        Iterator it = Plugins.servicesList.iterator();
        while (it.hasNext()) {
            if (!it.next().equals("com.db.persistence.ServerSchemeManifest"))
                it.remove();
        }
        ServerMain.loadProfile();
    }

    @Autowired private RuntimeValidator runtimeValidator;
    @Autowired private ObjectCrudSvc objectCrudSvc;
    @Autowired private QuerySvc querySvc;
    @Autowired private LoginSvc loginSvc;

    private String token;

    @Before
    public void LoadTest() {
        System.out.println("---------------------------------------------------------");
        System.out.println("Loaded Beans:");
        System.out.println(runtimeValidator);
        System.out.println(objectCrudSvc);
        System.out.println(loginSvc);
        System.out.println("---------------------------------------------------------");
//        LoginRestRequest loginRestRequest = new LoginRestRequest();
//        loginRestRequest.setUserName("talma5");
//        loginRestRequest.setApplicationName("Tester");
//        loginRestRequest.setTimeout(5000);
//        LoginRestResponse resp = loginSvc.login(loginRestRequest);
//        if (!resp.getReturnCode().equals(OK)) {
//            System.out.println("Failed to login: " + resp.getMessage());
//            // TODO: have better exception
//            throw new RuntimeException("Failed to run test");
//        }
//        token = resp.getToken();
        token = INTERNAL_SERVER_USER_TOKEN;

        objectCrudSvc.setToken(token);
        querySvc.setToken(token);
    }

    @Test
    public void ObjectCrudTest() throws Exception {
        new ObjectCrudSvcTest(objectCrudSvc).go();
    }

    @Test
    public void QuerySvcTest() throws Exception {
        new QuerySvcTest(querySvc, objectCrudSvc).go();
    }
}
