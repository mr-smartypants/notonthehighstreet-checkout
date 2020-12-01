package me.olhovsky.notonthehighstreet.checkout.rules;

import me.olhovsky.notonthehighstreet.checkout.Item;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static java.util.Collections.emptyList;
import static me.olhovsky.notonthehighstreet.checkout.TestConstants.CARD_HOLDER_ITEM;
import static me.olhovsky.notonthehighstreet.checkout.TestConstants.KIDS_TSHIRT_ITEM;
import static me.olhovsky.notonthehighstreet.checkout.TestConstants.PERSONALISED_CUFFLINKS_ITEM;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MultiPurchaseDiscountRuleTest {

    @Test
    void getPromotionValue_notEnoughEligibleItems_zeroReturned() {
        //given
        PromotionalRule rule = new MultiPurchaseDiscountRule(CARD_HOLDER_ITEM.getProductCode(), 2, BigDecimal.valueOf(8.5));

        //when
        List<Item> items = List.of(CARD_HOLDER_ITEM, PERSONALISED_CUFFLINKS_ITEM, KIDS_TSHIRT_ITEM);
        BigDecimal result = rule.getPromotionValue(items);

        //then
        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    void getPromotionValue_discountApplicable_discountValueReturned() {
        //given
        PromotionalRule rule = new MultiPurchaseDiscountRule(CARD_HOLDER_ITEM.getProductCode(), 2, BigDecimal.valueOf(8.5));

        //when
        List<Item> items = List.of(CARD_HOLDER_ITEM, CARD_HOLDER_ITEM, KIDS_TSHIRT_ITEM);
        BigDecimal result = rule.getPromotionValue(items);

        //then
        assertEquals(BigDecimal.valueOf(1.5).setScale(2), result);
    }

    @Test
    void getPromotionValue_discountApplicableButPromotionalPriceIsHigher_zeroReturned() {
        //given
        PromotionalRule rule = new MultiPurchaseDiscountRule(CARD_HOLDER_ITEM.getProductCode(), 2, BigDecimal.valueOf(10.5));

        //when
        List<Item> items = List.of(CARD_HOLDER_ITEM, CARD_HOLDER_ITEM, KIDS_TSHIRT_ITEM);
        BigDecimal result = rule.getPromotionValue(items);

        //then
        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    void getPromotionValue_emptyItems_zeroReturned() {
        //given
        PromotionalRule rule = new MultiPurchaseDiscountRule(CARD_HOLDER_ITEM.getProductCode(), 2, BigDecimal.valueOf(8.5));

        //when
        BigDecimal result = rule.getPromotionValue(emptyList());

        //then
        assertEquals(BigDecimal.ZERO, result);
    }


    @Test
    void getPromotionValue_illegalArguments_exceptionThrown() {
        assertThrows(IllegalArgumentException.class, () -> new MultiPurchaseDiscountRule(CARD_HOLDER_ITEM.getProductCode(), 0, BigDecimal.valueOf(8.5)));
    }

}