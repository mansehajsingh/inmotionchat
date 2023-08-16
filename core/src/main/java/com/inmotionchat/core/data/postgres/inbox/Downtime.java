package com.inmotionchat.core.data.postgres.inbox;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inmotionchat.core.data.Schema;
import com.inmotionchat.core.data.dto.DowntimeDTO;
import com.inmotionchat.core.data.postgres.identity.Tenant;
import com.inmotionchat.core.exceptions.DomainInvalidException;
import com.inmotionchat.core.models.TimeOfDay;
import com.inmotionchat.core.util.validation.Violation;
import com.inmotionchat.smartpersist.ConstraintPrefix;
import jakarta.persistence.*;

@Entity
@Table(name = "downtime", schema = Schema.InboxManagement)
public class Downtime {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = ConstraintPrefix.FKEY + "tenant"))
    @JsonIgnore
    private Tenant tenant;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = ConstraintPrefix.FKEY + "inbox_group"))
    @JsonIgnore
    private InboxGroup inboxGroup;

    private String startTime;

    private String endTime;

    public Downtime() {}

    public Downtime(InboxGroup inboxGroup, DowntimeDTO proto) {
        this(inboxGroup, new TimeOfDay(proto.startTime()), new TimeOfDay(proto.endTime()));
    }

    public Downtime(InboxGroup inboxGroup, TimeOfDay start, TimeOfDay end) {
        this.tenant = inboxGroup.getTenant();
        this.inboxGroup = inboxGroup;
        this.startTime = start.toString();
        this.endTime = end.toString();
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public InboxGroup getInboxGroup() {
        return this.inboxGroup;
    }

    public void setInboxGroup(InboxGroup inboxGroup) {
        this.tenant = inboxGroup.getTenant();
        this.inboxGroup = inboxGroup;
    }

    public Tenant getTenant() {
        return this.tenant;
    }

    public TimeOfDay getStartTime() {
        return new TimeOfDay(startTime);
    }

    public void setStartTime(TimeOfDay start) {
        this.startTime = start.toString();
    }

    public TimeOfDay getEndTime() {
        return new TimeOfDay(this.endTime);
    }

    public void setEndTime(TimeOfDay end) {
        this.endTime = end.toString();
    }

    public boolean overlaps(Downtime other) {
        boolean startsBeforeOrDuring = other.getEndTime().isAfter(this.getStartTime()) || other.getEndTime().equals(this.getStartTime());
        boolean endsAfterOrDuring = other.getStartTime().isBefore(this.getEndTime()) || other.getStartTime().equals(this.getEndTime());
        return startsBeforeOrDuring && endsAfterOrDuring;
    }

    public void validate() throws DomainInvalidException {
        if (this.startTime == null || this.endTime == null)
            throw new DomainInvalidException(new Violation("time", null, "Downtime cannot have a null end time or start time."));

        if (this.getStartTime().isAfter(this.getEndTime()) || this.getStartTime().equals(this.getEndTime())) {
            throw new DomainInvalidException(new Violation("startTime", startTime, "Start time must be strictly before end time."));
        }
    }

}
