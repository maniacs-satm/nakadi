package org.zalando.nakadi.validation;

import com.google.common.collect.Lists;
import org.json.JSONObject;
import org.zalando.nakadi.validation.schema.JsonAttributeConstraint;
import org.zalando.nakadi.validation.schema.SchemaConstraint;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SchemaCompatibilityChecker {

    final private Stream<JsonAttributeConstraint> CONSTRAINTS = Lists.newArrayList(
            new JsonAttributeConstraint("not"),
            new JsonPropertyConstraint())
            .stream();

    public List<SchemaIncompatibility> isCompatible(final JSONObject oldSchema, final JSONObject newSchema) {
        return new ArrayList<>();
    }

    public Optional<SchemaIncompatibility> checkConstraints(final JSONObject schema) {
        return schema.toMap().entrySet().stream()
                .map(this::validate)
                .findFirst()
                .orElse(Optional.empty());
    }

    private Optional<SchemaIncompatibility> validate(final Map.Entry<String, Object> jsonProperty) {
        return CONSTRAINTS.map(c -> c.validate(jsonProperty)).findFirst().orElse(Optional.empty());
    }
}
