package ServerIntegrationTest;

import com.db.persistence.services.ObjectCrudSvc;
import com.db.persistence.services.QuerySvc;
import com.db.server.DroneServer;
import com.generic_tools.validations.RuntimeValidator;
import com.plugins_manager.Plugins;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Iterator;

//@RunWith(SpringJUnit4ClassRunner.class)
@RunWith(SpringRunner.class)
//@ActiveProfiles({H2, Hibernate})
@ContextConfiguration(classes = DroneServer.class)
public class IntegrationTest {

    static {
        Iterator it = Plugins.servicesList.iterator();
        while (it.hasNext()) {
            if (!it.next().equals("com.db.persistence.ServerSchemeManifest"))
                it.remove();
        }
        DroneServer.loadProfile();
    }

    @Autowired private RuntimeValidator runtimeValidator;
    @Autowired private ObjectCrudSvc objectCrudSvc;
    @Autowired private QuerySvc querySvc;

    @Before
    public void LoadTest() {
        System.out.println("---------------------------------------------------------");
        System.out.println("Loaded Beans:");
        System.out.println(runtimeValidator);
        System.out.println(objectCrudSvc);
        System.out.println("---------------------------------------------------------");
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
