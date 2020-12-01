package me.olhovsky.notonthehighstreet.checkout;

import me.olhovsky.notonthehighstreet.checkout.rules.MultiPurchaseDiscountRule;
import me.olhovsky.notonthehighstreet.checkout.rules.PromotionalRule;
import me.olhovsky.notonthehighstreet.checkout.rules.TotalAmountDiscountRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CheckoutTest {

    private Checkout checkout;

    @BeforeEach
    void setUp() {
        Set<PromotionalRule> rules = Set.of(new MultiPurchaseDiscountRule("001", 2, BigDecimal.valueOf(8.5)),
                new TotalAmountDiscountRule(BigDecimal.valueOf(60), BigDecimal.valueOf(10)));
        checkout = new Checkout(rules);
    }

    @Test
    void total_eligibleForTotalAmountPromotion_promotionApplied() {
        //given
        checkout.scan(new Item("001", "Travel Card Holder", BigDecimal.valueOf(9.25)));
        checkout.scan(new Item("002", "Personalised cufflinks", BigDecimal.valueOf(45.00)));
        checkout.scan(new Item("003", "Kids T-shirt", BigDecimal.valueOf(19.95)));

        //when
        Double result = checkout.total();

        //then
        assertEquals(66.78, result);
    }

    @Test
    void total_eligibleForMultiPurchasePromotion_promotionApplied() {
        //given
        checkout.scan(new Item("001", "Travel Card Holder", BigDecimal.valueOf(9.25)));
        checkout.scan(new Item("003", "Kids T-shirt", BigDecimal.valueOf(19.95)));
        checkout.scan(new Item("001", "Travel Card Holder", BigDecimal.valueOf(9.25)));

        //when
        Double result = checkout.total();

        //then
        assertEquals(36.95, result);
    }

    @Test
    void total_eligibleForBothPromotions_bothPromotionsAppliedInCorrectOrder() {
        //given
        checkout.scan(new Item("001", "Travel Card Holder", BigDecimal.valueOf(9.25)));
        checkout.scan(new Item("002", "Personalised cufflinks", BigDecimal.valueOf(45.00)));
        checkout.scan(new Item("001", "Travel Card Holder", BigDecimal.valueOf(9.25)));
        checkout.scan(new Item("003", "Kids T-shirt", BigDecimal.valueOf(19.95)));

        //when
        Double result = checkout.total();

        //then
        assertEquals(73.76, result);
    }

    @Test
    void total_notEligibleForAnyPromotion_totalValueReturned() {
        //given
        checkout.scan(new Item("001", "Travel Card Holder", BigDecimal.valueOf(9.25)));
        checkout.scan(new Item("003", "Kids T-shirt", BigDecimal.valueOf(19.95)));

        //when
        Double result = checkout.total();

        //then
        assertEquals(29.20, result);
    }

    @Test
    void total_noItemsScanned_zeroReturned() {
        //when
        Double result = checkout.total();

        //then
        assertEquals(0, result);
    }
}