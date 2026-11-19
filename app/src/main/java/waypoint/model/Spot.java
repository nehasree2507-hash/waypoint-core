
package waypoint.model;

import java.util.Objects;

 /** ENTITY. Identity = id. Invariant: a spot cannot be assigned while occupied. */
public final class Spot {
    private final SpotId id;
    private VehicleId occupant; // The only mutable field

    // Constructor validates that id is not null
    public Spot(SpotId id) {
        this.id = Objects.requireNonNull(id, "id cannot be null");
    }

    // Assigns a vehicle only if the spot is currently free
    public boolean tryAssign(VehicleId v) {
        Objects.requireNonNull(v, "vehicle cannot be null");

        if (occupant != null) {
            return false;
        }

        occupant = v;
        return true;
    }

    // Releases the spot only when the given vehicle occupies it
    public void release(VehicleId v) {
        if (Objects.equals(occupant, v)) {
            occupant = null;
        }
    }

    // Returns true when no vehicle occupies the spot
    public boolean isFree() {
        return occupant == null;
    }

    // Two spots are equal when their IDs are equal
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Spot other)) return false;
        return id.equals(other.id);
    }

    // Hash code depends only on ID
    @Override
    public int hashCode() {
        return id.hashCode();
    }
}