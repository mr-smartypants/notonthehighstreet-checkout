package me.olhovsky.notonthehighstreet.checkout.rules;

import lombok.NonNull;
import me.olhovsky.notonthehighstreet.checkout.Item;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class TotalAmountDiscountRule implements PromotionalRule {
    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);

    private final BigDecimal threshold;
    private final BigDecimal discountPercent;

    public TotalAmountDiscountRule(@NonNull BigDecimal threshold, @NonNull BigDecimal discountPercent) {
        if (discountPercent.compareTo(HUNDRED) > 0) {
            throw new IllegalArgumentException("Discount value cannot be more than 100%");
        }
        if (threshold.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Threshold cannot be negative");
        }

        this.threshold = threshold;
        this.discountPercent = discountPercent;
    }

    @Override
    public int getPriority() {
        return 20;
    }

    @Override
    public BigDecimal getPromotionValue(@NonNull List<Item> items) {
        BigDecimal total = items.stream()
                .map(Item::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (total.compareTo(threshold) < 0) {
            return BigDecimal.ZERO;
        }

        return total.multiply(discountPercent).divide(HUNDRED, RoundingMode.HALF_DOWN);
    }
}
