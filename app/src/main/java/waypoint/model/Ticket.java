
package waypoint.model;

import java.time.Instant;
import java.util.Objects;

/** ENTITY. Invariants:
 *   I1  entry is always present
 *   I2  exit is null, or not before entry
 *   I3  fare is null, or not negative
 *   I4  fare exists only if exit exists
 *   I5  a closed ticket never reopens
 */
public final class Ticket {

    private final TicketId id;       // Ticket identity never changes
    private final SpotId spot;       // Assigned spot never changes
    private final Instant entry;     // Entry time never changes

    private Instant exit;            // Mutable: set when ticket closes
    private Money fare;               // Mutable: set when ticket closes

    // Private constructor: objects must be created using open()
    private Ticket(TicketId id, SpotId spot, Instant entry) {
        this.id = Objects.requireNonNull(id, "id cannot be null");
        this.spot = Objects.requireNonNull(spot, "spot cannot be null");
        this.entry = Objects.requireNonNull(entry, "entry cannot be null");
        this.exit = null;
        this.fare = null;
    }

    // Factory method to create a new open ticket
    public static Ticket open(TicketId id, SpotId spot, Instant entry) {
        return new Ticket(id, spot, entry);
    }

    // Returns true if the ticket has not yet been closed
    public boolean isOpen() {
        return exit == null;
    }

    // Returns the fare; null while the ticket is open
    public Money fare() {
        return fare;
    }

    // Closes the ticket and calculates its fare
    public Money close(Instant at, FarePolicy policy) {

        Objects.requireNonNull(at, "exit time cannot be null");
        Objects.requireNonNull(policy, "fare policy cannot be null");

        // A closed ticket cannot be closed again
        if (!isOpen()) {
            throw new TicketAlreadyClosed();
        }

        // Exit time cannot be earlier than entry time
        if (at.isBefore(entry)) {
            throw new IllegalArgumentException("Exit cannot be before entry");
        }

        // Calculate and validate fare before changing ticket state
        Money calculatedFare = Objects.requireNonNull(
            policy.fareFor(entry, at, spot),
            "fare cannot be null"
        );

        if (calculatedFare.isNegative()) {
            throw new IllegalArgumentException("Fare cannot be negative");
        }

        // Update both mutable fields only after successful validation
        this.exit = at;
        this.fare = calculatedFare;

        return this.fare;
    }
}

// Exception thrown when attempting to close an already closed ticket
class TicketAlreadyClosed extends IllegalStateException {
    public TicketAlreadyClosed() {
        super("Ticket is already closed");
    }
}