package com.db.persistence.wsRest;

import com.db.persistence.scheme.BaseObject;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class QueryResponseRemote {

	private List<BaseObject> resultListBase;
	
	public QueryResponseRemote() {
		resultListBase = new ArrayList<BaseObject>();
	}
	
	public void setResult(List<? extends BaseObject> lst) {
		resultListBase.addAll(lst);
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
		return resultListBase;
	}
}
