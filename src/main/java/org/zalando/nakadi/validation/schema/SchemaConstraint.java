package org.zalando.nakadi.validation.schema;

import org.zalando.nakadi.validation.SchemaIncompatibility;

import java.util.Map;
import java.util.Optional;

public interface SchemaConstraint {
    Optional<SchemaIncompatibility> validate(final Map.Entry<String, Object> jsonProperty);
}
