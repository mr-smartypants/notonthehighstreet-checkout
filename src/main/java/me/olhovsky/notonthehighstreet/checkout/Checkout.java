package me.olhovsky.notonthehighstreet.checkout;

import lombok.NonNull;
import me.olhovsky.notonthehighstreet.checkout.rules.PromotionalRule;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class Checkout {
    private final Set<PromotionalRule> promotionalRules;
    private final List<Item> items = new ArrayList<>();

    public Checkout(@NonNull Set<PromotionalRule> promotionalRules) {
        this.promotionalRules = Set.copyOf(promotionalRules);
    }

    public void scan(@NonNull Item item) {
        items.add(item);
    }

    public Double total() {
        List<Item> itemsWithPromotions = new ArrayList<>(items);
        promotionalRules.stream()
                .sorted(Comparator.comparing(PromotionalRule::getPriority))
                .forEach(p -> {
                    BigDecimal discount = p.getPromotionValue(itemsWithPromotions);
                    itemsWithPromotions.add(new Item("Promo", "Promotion", discount.negate()));
                });

        return itemsWithPromotions.stream()
                .map(Item::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }

}
