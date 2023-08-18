package com.inmotionchat.core.data.postgres.journey.story;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.inmotionchat.core.data.Schema;
import com.inmotionchat.core.data.postgres.identity.Tenant;
import com.inmotionchat.smartpersist.ConstraintPrefix;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.ZonedDateTime;
import java.util.Map;

@Entity
@Table(name = "story_timeline_events", schema = Schema.JourneyManagement)
public class StoryTimelineEvent {

    private static final ObjectMapper mapper = new ObjectMapper() {{
        configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }};

    @Id
    @GeneratedValue
    private Long id;

    @CreationTimestamp
    @JsonIgnore
    private ZonedDateTime loggedAt;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = ConstraintPrefix.FKEY + "tenant"))
    @JsonIgnore
    private Tenant tenant;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = ConstraintPrefix.FKEY + "story"))
    @JsonIgnore
    private Story story;

    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> template;

    protected StoryTimelineEvent() {}

    public StoryTimelineEvent(Tenant tenant, Story story, Object template) {
        this.tenant = tenant;
        this.story = story;
        this.template = mapper.convertValue(template, Map.class);
    }

    public Long getId() {
        return id;
    }

    public ZonedDateTime getLoggedAt() {
        return loggedAt;
    }

    @JsonProperty("loggedAt")
    protected String getCreatedAtString() {
        return loggedAt.toString();
    }

    public Tenant getTenant() {
        return tenant;
    }

    public Story getStory() {
        return story;
    }

    public Map<String, Object> getTemplate() {
        return template;
    }

}
