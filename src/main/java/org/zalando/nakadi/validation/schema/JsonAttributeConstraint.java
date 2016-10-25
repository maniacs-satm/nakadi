package org.zalando.nakadi.validation.schema;

import org.zalando.nakadi.validation.SchemaIncompatibility;

import java.util.Map;
import java.util.Optional;

public class JsonAttributeConstraint implements SchemaConstraint {
    private final String attribute;

    public JsonAttributeConstraint(final String attribute) {
        this.attribute = attribute;
    }

    @Override
    public Optional<SchemaIncompatibility> validate(final Map.Entry<String, Object> jsonProperty) {
        if (this.attribute.equals(jsonProperty.getKey())) {
            return Optional.of(new ForbiddenAttributeIncompatibility(this.attribute));
        } else {
            return Optional.empty();
        }
    }
}
