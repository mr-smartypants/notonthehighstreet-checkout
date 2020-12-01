package me.olhovsky.notonthehighstreet.checkout.rules;

import lombok.NonNull;
import me.olhovsky.notonthehighstreet.checkout.Item;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class MultiPurchaseDiscountRule implements PromotionalRule {
    private final String itemProductionCode;
    private final int itemCountThreshold;
    private final BigDecimal promotionalPrice;

    public MultiPurchaseDiscountRule(@NonNull String itemProductionCode,
                                     int itemCountThreshold,
                                     @NonNull  BigDecimal promotionalPrice) {
        if (itemCountThreshold < 1) {
            throw new IllegalArgumentException("Item count threshold cannot be less than 1");
        }

        this.itemProductionCode = itemProductionCode;
        this.itemCountThreshold = itemCountThreshold;
        this.promotionalPrice = promotionalPrice;
    }

    @Override
    public int getPriority() {
        return 10;
    }

    @Override
    public BigDecimal getPromotionValue(@NonNull List<Item> items) {
        List<Item> discountedItems = items.stream()
                .filter(i -> i.getProductCode().equals(itemProductionCode))
                .collect(Collectors.toList());
        if (discountedItems.size() < itemCountThreshold) {
            return BigDecimal.ZERO;
        }

        return discountedItems.stream()
                .map(Item::getPrice)
                .map(p -> p.subtract(promotionalPrice))
                .filter(d -> d.compareTo(BigDecimal.ZERO) > 0)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
