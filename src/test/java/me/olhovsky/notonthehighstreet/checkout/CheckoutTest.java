package me.olhovsky.notonthehighstreet.checkout;

import me.olhovsky.notonthehighstreet.checkout.rules.MultiPurchaseDiscountRule;
import me.olhovsky.notonthehighstreet.checkout.rules.PromotionalRule;
import me.olhovsky.notonthehighstreet.checkout.rules.TotalAmountDiscountRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static me.olhovsky.notonthehighstreet.checkout.TestConstants.CARD_HOLDER_ITEM;
import static me.olhovsky.notonthehighstreet.checkout.TestConstants.KIDS_TSHIRT_ITEM;
import static me.olhovsky.notonthehighstreet.checkout.TestConstants.PERSONALISED_CUFFLINKS_ITEM;
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
        checkout.scan(CARD_HOLDER_ITEM);
        checkout.scan(PERSONALISED_CUFFLINKS_ITEM);
        checkout.scan(KIDS_TSHIRT_ITEM);

        //when
        Double result = checkout.total();

        //then
        assertEquals(66.78, result);
    }

    @Test
    void total_eligibleForMultiPurchasePromotion_promotionApplied() {
        //given
        checkout.scan(CARD_HOLDER_ITEM);
        checkout.scan(KIDS_TSHIRT_ITEM);
        checkout.scan(CARD_HOLDER_ITEM);

        //when
        Double result = checkout.total();

        //then
        assertEquals(36.95, result);
    }

    @Test
    void total_eligibleForBothPromotions_bothPromotionsAppliedInCorrectOrder() {
        //given
        checkout.scan(CARD_HOLDER_ITEM);
        checkout.scan(PERSONALISED_CUFFLINKS_ITEM);
        checkout.scan(CARD_HOLDER_ITEM);
        checkout.scan(KIDS_TSHIRT_ITEM);

        //when
        Double result = checkout.total();

        //then
        assertEquals(73.76, result);
    }

    @Test
    void total_notEligibleForAnyPromotion_totalValueReturned() {
        //given
        checkout.scan(CARD_HOLDER_ITEM);
        checkout.scan(KIDS_TSHIRT_ITEM);

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