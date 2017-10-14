package com.db.persistence.wsSoap;

import com.db.persistence.PluginManifest;
import com.db.persistence.scheme.BaseObject;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@PluginManifest.WSImporter
@XmlRootElement
public class QueryResponseRemote implements Serializable {

//	@JsonValue
	private List<BaseObject> resultList;
	
	public QueryResponseRemote() {
		resultList = new ArrayList();
	}
	
	public void setResultList(List<? extends BaseObject> lst) {

		resultList.addAll(lst);
//		resultList = new Integer[lst.size()];
//		
//		for (int i = 0 ; i < lst.size() ; i++) {
//			resultList[i] = lst.get(i).getObjId();
//		}
	}

	@XmlElement(required = true)
	public List<BaseObject> getResultList() {
//		List<Integer> lst = new ArrayList<>();
//		lst.addAll(Arrays.asList(resultList));
//		return lst;
		return resultList;
	}
}
