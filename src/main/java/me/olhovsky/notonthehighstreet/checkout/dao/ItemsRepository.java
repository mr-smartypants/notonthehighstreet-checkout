package me.olhovsky.notonthehighstreet.checkout.dao;

import me.olhovsky.notonthehighstreet.checkout.Item;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

public class ItemsRepository {
    private final Map<String, Item> items = Map.of(
            "001", new Item("001", "Travel Card Holder", BigDecimal.valueOf(9.25)),
            "002", new Item("002", "Personalised cufflinks", BigDecimal.valueOf(45.00)),
            "003", new Item("003", "Kids T-shirt", BigDecimal.valueOf(19.95)));

    public Optional<Item> getItem(String productionCode) {
        return Optional.ofNullable(items.get(productionCode));
    }
}
