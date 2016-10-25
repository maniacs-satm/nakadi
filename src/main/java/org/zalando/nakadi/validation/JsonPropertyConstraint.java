package org.zalando.nakadi.validation;

import org.json.JSONObject;
import org.zalando.nakadi.validation.schema.SchemaConstraint;

import java.util.Map;
import java.util.Optional;

public class JsonPropertyConstraint implements SchemaConstraint {
    final private SchemaCompatibilityChecker checker;
    private final String attribute;

    public JsonPropertyConstraint(final String attribute, final SchemaCompatibilityChecker checker) {
        this.attribute = attribute;
        this.checker = checker;
    }

    @Override
    public Optional<SchemaIncompatibility> validate(final Map.Entry<String, Object> jsonProperty) {
        if (this.attribute.equals(jsonProperty.getKey())) {
            // TODO: check that schema validator guarantees "property" to always have an object as value
            return checker.checkConstraints((JSONObject) jsonProperty.getValue());
            } else {
                return
            }
        }
        return null;
    }
}
