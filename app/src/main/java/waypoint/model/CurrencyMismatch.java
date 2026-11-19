
package waypoint.model;

public class CurrencyMismatch extends RuntimeException {
    public CurrencyMismatch() {
        super("Cannot perform operations with different currencies");
    }
}