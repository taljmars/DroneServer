package com.dronedb.persistence.scheme.apis;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.dronedb.persistence.scheme.BaseObject;

@XmlRootElement
public class QueryResponseRemote {
	
	@XmlElement(required = true)	
	private List<BaseObject> resultListBase;
	
	public QueryResponseRemote() {
		resultListBase = new ArrayList();
	}
	
	public void setResult(List<? extends BaseObject> lst) {
		resultListBase.addAll(lst);
//		resultList = new Integer[lst.size()];
//		
//		for (int i = 0 ; i < lst.size() ; i++) {
//			resultList[i] = lst.get(i).getObjId();
//		}
	}
	
	public List<BaseObject> getResultList() {
//		List<Integer> lst = new ArrayList<>();
//		lst.addAll(Arrays.asList(resultList));
//		return lst;
		return resultListBase;
	}
}
