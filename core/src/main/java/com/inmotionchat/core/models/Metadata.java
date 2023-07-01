package com.inmotionchat.core.models;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.ZonedDateTime;

public class Metadata {

    public static class MetadataSerializer extends StdSerializer<Metadata> {

        public MetadataSerializer() {
            this(null);
        }

        public MetadataSerializer(Class<Metadata> t) {
            super(t);
        }

        @Override
        public void serialize(Metadata metadata, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
                throws IOException
        {
            jsonGenerator.writeStartObject();

            if (metadata.createdAt != null)
                jsonGenerator.writeStringField("createdAt", metadata.createdAt.toString());
            else
                jsonGenerator.writeNullField("createdAt");

            if (metadata.lastModifiedAt != null)
                jsonGenerator.writeStringField("lastModifiedAt", metadata.lastModifiedAt.toString());
            else
                jsonGenerator.writeNullField("lastModifiedAt");

            if (metadata.createdBy != null)
                jsonGenerator.writeNumberField("createdBy", metadata.createdBy);
            else
                jsonGenerator.writeNullField("createdBy");

            if (metadata.lastModifiedBy != null)
                jsonGenerator.writeNumberField("lastModifiedBy", metadata.lastModifiedBy);
            else
                jsonGenerator.writeNullField("lastModifiedBy");

            jsonGenerator.writeEndObject();
        }
    }

    public final ZonedDateTime createdAt;

    public final ZonedDateTime lastModifiedAt;

    public final Long lastModifiedBy;

    public Long createdBy;

    public Metadata(ZonedDateTime createdAt, ZonedDateTime lastModifiedAt, Long createdBy, Long lastModifiedBy) {
        this.createdAt = createdAt;
        this.lastModifiedAt = lastModifiedAt;
        this.createdBy = createdBy;
        this.lastModifiedBy = lastModifiedBy;
    }

}
