package waypoint.model;

/** VALUE OBJECT. TODO: validate and normalise so an invalid instance cannot exist. */
public record Registration(String value) {
    // Format: two letters, two digits, one or two letters, four digits. e.g. MH12AB1234
    // TODO: compact constructor — requireNonNull, trim, upper-case, then validate.
}