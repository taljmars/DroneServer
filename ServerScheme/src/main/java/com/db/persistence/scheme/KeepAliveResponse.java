package com.db.persistence.scheme;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@XmlRootElement
public class KeepAliveResponse {

    private Date serverDate;
    private String message;
    private Integer returnCode;

    public KeepAliveResponse() {
    }

    public void setServerDate(Date serverDate) {
        this.serverDate = serverDate;
    }

    public Date getServerDate() {
        return serverDate;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public Integer getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(Integer returnCode) {
        this.returnCode = returnCode;
    }
}
