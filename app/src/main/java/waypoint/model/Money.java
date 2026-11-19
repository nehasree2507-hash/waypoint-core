
package waypoint.model;

import java.util.*;

// Money is an immutable value object.
public final class Money implements Comparable<Money> {

    // Store money in paise. final prevents modification.
    private final long paise;

    // Store the currency, such as INR or USD.
    private final Currency currency;

    // Private constructor prevents direct object creation.
    private Money(long paise, Currency currency) {
        this.paise = paise;
        this.currency = Objects.requireNonNull(currency, "Currency cannot be null");
    }

    // Create money using the smallest currency unit (paise/cents).
    public static Money ofMinor(long amount, Currency currency) {
        return new Money(amount, currency);
    }

    // Create Indian rupees, converting rupees into paise.
    public static Money rupees(long amount) {
        return new Money(Math.multiplyExact(amount, 100L),
                Currency.getInstance("INR"));
    }

    // Add two Money objects.
    public Money plus(Money other) {
        checkCurrency(other);
        return new Money(Math.addExact(this.paise, other.paise), currency);
    }

    // Subtract another Money object.
    public Money minus(Money other) {
        checkCurrency(other);
        return new Money(Math.subtractExact(this.paise, other.paise), currency);
    }

    // Multiply money by an integer.
    public Money times(int multiplier) {
        return new Money(Math.multiplyExact(paise, multiplier), currency);
    }

    // Check whether the amount is negative.
    public boolean isNegative() {
        return paise < 0;
    }

    // Compare two Money objects.
    @Override
    public int compareTo(Money other) {
        checkCurrency(other);
        return Long.compare(this.paise, other.paise);
    }

    // Allocate money equally without losing any paise.
    public List<Money> allocate(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Number of shares must be positive");
        }

        List<Money> shares = new ArrayList<>();

        long quotient = paise / n;
        long remainder = paise % n;

        for (int i = 0; i < n; i++) {
            long amount = quotient;

            if (i < Math.abs(remainder)) {
                amount += Long.signum(remainder);
            }

            shares.add(new Money(amount, currency));
        }

        return List.copyOf(shares);
    }

    // Ensure both amounts use the same currency.
    private void checkCurrency(Money other) {
        Objects.requireNonNull(other, "Money cannot be null");

        if (!currency.equals(other.currency)) {
            throw new CurrencyMismatch();
        }
    }

    // Compare Money objects based on amount and currency.
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        if (!(obj instanceof Money other)) return false;

        return paise == other.paise &&
                currency.equals(other.currency);
    }

    // Generate hash code consistent with equals.
    @Override
    public int hashCode() {
        return Objects.hash(paise, currency);
    }

    // Display money in readable format.
    @Override
    public String toString() {
        return currency.getCurrencyCode() + " " +
                currency.getSymbol() + " " +
                paise / 100 + "." +
                String.format("%02d", Math.abs(paise % 100));
    }
}