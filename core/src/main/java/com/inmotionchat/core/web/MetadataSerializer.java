package com.inmotionchat.core.web;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.inmotionchat.core.domains.models.Metadata;

import java.io.IOException;

public class MetadataSerializer extends StdSerializer<Metadata> {

    public MetadataSerializer() {
        this(null);
    }

    protected MetadataSerializer(Class<Metadata> t) {
        super(t);
    }

    @Override
    public void serialize(Metadata metadata, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();

        jsonGenerator.writeNumberField("createdBy", metadata.createdBy.getId());
        jsonGenerator.writeStringField("createdAt", metadata.createdAt.toString());

        if (metadata.lastUpdatedBy == null) {
            jsonGenerator.writeNullField("lastUpdatedBy");
        } else {
            jsonGenerator.writeNumberField("lastUpdatedBy", metadata.lastUpdatedBy.getId());
        }

        if (metadata.lastUpdatedAt == null) {
            jsonGenerator.writeNullField("lastUpdatedAt");
        } else {
            jsonGenerator.writeStringField("lastUpdatedAt", metadata.lastUpdatedAt.toString());
        }

        jsonGenerator.writeEndObject();
    }

}