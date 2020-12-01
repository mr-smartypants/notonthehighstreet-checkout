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

class TotalAmountDiscountRuleTest {

    @Test
    void getPromotionValue_totalValueBelowThreshold_zeroReturned() {
        //given
        PromotionalRule rule = new TotalAmountDiscountRule(BigDecimal.valueOf(60), BigDecimal.TEN);

        //when
        List<Item> items = List.of(CARD_HOLDER_ITEM, KIDS_TSHIRT_ITEM, CARD_HOLDER_ITEM);
        BigDecimal result = rule.getPromotionValue(items);

        //then
        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    void getPromotionValue_totalValueAboveThreshold_discountValueReturned() {
        //given
        PromotionalRule rule = new TotalAmountDiscountRule(BigDecimal.valueOf(60), BigDecimal.TEN);

        //when
        List<Item> items = List.of(CARD_HOLDER_ITEM, PERSONALISED_CUFFLINKS_ITEM, KIDS_TSHIRT_ITEM);
        BigDecimal result = rule.getPromotionValue(items);

        //then
        assertEquals(BigDecimal.valueOf(7.42), result);
    }

    @Test
    void getPromotionValue_zeroThresholdAndNoItems_zeroReturned() {
        //given
        PromotionalRule rule = new TotalAmountDiscountRule(BigDecimal.ZERO, BigDecimal.TEN);

        //when
        BigDecimal result = rule.getPromotionValue(emptyList());

        //then
        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    void getPromotionValue_zeroDiscountValue_zeroReturned() {
        //given
        PromotionalRule rule = new TotalAmountDiscountRule(BigDecimal.valueOf(10), BigDecimal.ZERO);

        //when
        List<Item> items = List.of(CARD_HOLDER_ITEM, PERSONALISED_CUFFLINKS_ITEM, KIDS_TSHIRT_ITEM);
        BigDecimal result = rule.getPromotionValue(items);

        //then
        assertEquals(BigDecimal.ZERO.setScale(2), result);
    }

    @Test
    void getPromotionValue_illegalArguments_exceptionThrown() {
        assertThrows(IllegalArgumentException.class, () -> new TotalAmountDiscountRule(BigDecimal.valueOf(-10), BigDecimal.TEN));
        assertThrows(IllegalArgumentException.class, () -> new TotalAmountDiscountRule(BigDecimal.valueOf(60), BigDecimal.valueOf(120)));
    }

}