package com.dronedb.persistence.triggers;

import org.springframework.context.ApplicationContext;

import com.dronedb.persistence.services.DroneDbCrudSvc;
import com.dronedb.persistence.services.QuerySvc;
import com.dronedb.triggers.DeleteObjectTrigger;
import com.dronedb.persistence.scheme.BaseObject;
import com.dronedb.persistence.scheme.Mission;

public class DeleteObjectTriggerImpl implements DeleteObjectTrigger {
	
	private QuerySvc querySvc;
	private DroneDbCrudSvc droneDbCrudSvc;
	
	private ApplicationContext applicationContext;
	
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends BaseObject> void handleDeleteObject(T object) {
		System.out.println("Int test " + object.toString());
		
		if (!(object instanceof Mission))
			return;
		
		querySvc = (QuerySvc) applicationContext.getBean("querySvc");
		droneDbCrudSvc = (DroneDbCrudSvc) applicationContext.getBean("droneDbCrudSvc");
		
		Mission mission = (Mission) object;
		
//		QueryRequest queryRequest = new QueryRequest();
//		queryRequest.setClz(MissionItem.class);
//		queryRequest.setQuery("getMissionItemsyMissionUUID");
//		queryRequest.setParameters(Arrays.asList(mission.getObjId()));
//		List<MissionItem> list = (List<MissionItem>) querySvc.query(queryRequest);
//		
//		for (MissionItem missionItem : list)
//			droneDbCrudSvc.delete(missionItem);
	}


}
