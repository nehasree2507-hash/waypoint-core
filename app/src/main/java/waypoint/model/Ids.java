
package waypoint.model;

// TicketId represents a unique ticket identifier.
record TicketId(String value) {
    TicketId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("TicketId cannot be null or blank");
        }
    }
}

// SpotId represents a unique parking spot identifier.
record SpotId(String value) {
    SpotId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("SpotId cannot be null or blank");
        }
    }
}

// VehicleId represents a unique vehicle identifier.
record VehicleId(String value) {
    VehicleId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("VehicleId cannot be null or blank");
        }
    }
}