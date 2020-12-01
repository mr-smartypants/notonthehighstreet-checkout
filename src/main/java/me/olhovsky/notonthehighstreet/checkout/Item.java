package me.olhovsky.notonthehighstreet.checkout;

import lombok.Value;

import java.math.BigDecimal;

@Value()
public class Item {
    String productCode;
    String name;
    BigDecimal price;
}
