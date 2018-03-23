package com.auditdb.persistence.scheme;

import com.auditdb.persistence.base_scheme.EventLogObject;
import com.db.persistence.scheme.BaseObject;
import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;

import javax.persistence.*;
import java.util.Objects;

@NamedNativeQueries({
        @NamedNativeQuery(
                name = "GetAllRegistrationLog",
                query = "SELECT * FROM RegistrationLog",
                resultClass = RegistrationLog.class
        ),
        @NamedNativeQuery(
                name = "GetAllRegistrationLog_BetweenDates",
                query = "SELECT * FROM RegistrationLog WHERE eventTime BETWEEN :START_DATE AND :END_DATE",
                resultClass = RegistrationLog.class
        )
})
@Access(AccessType.FIELD)
@Entity
@Table
public class RegistrationLog extends EventLogObject {

    private String description;

    public RegistrationLog() {
        super();
    }

    public RegistrationLog(RegistrationLog registrationLog) {
        super(registrationLog);
        this.description = registrationLog.description;
    }

    @Override
    public RegistrationLog clone() {
        return new RegistrationLog(this);
    }

    @Override
    public BaseObject copy() {
        RegistrationLog registrationLog = (RegistrationLog) super.copy();
        registrationLog.description = this.description;
        return registrationLog;
    }

    @Override
    public void set(BaseObject baseObject) {
        super.set(baseObject);
        RegistrationLog registrationLog = (RegistrationLog) baseObject;
        this.description = registrationLog.description;
    }

    @Getter
    public String getDescription() {
        return description;
    }

    @Setter
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        RegistrationLog registrationLog = (RegistrationLog) o;
        return  super.equals(o) && Objects.equals(description, registrationLog.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), description);
    }

    @Override
    public String toString() {
        return "RegistrationLog{" +
                super.toString() +
                ", description=" + description +
                ", deleted=" + deleted +
                ", fromRevision=" + fromRevision +
                ", clz=" + clz +
                '}';
    }
}
