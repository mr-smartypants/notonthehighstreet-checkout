package me.olhovsky.notonthehighstreet.checkout.rules;

import me.olhovsky.notonthehighstreet.checkout.Item;

import java.math.BigDecimal;
import java.util.List;

public interface PromotionalRule {
    int getPriority();

    BigDecimal getPromotionValue(List<Item> items);
}
