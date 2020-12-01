package me.olhovsky.notonthehighstreet.checkout;

import me.olhovsky.notonthehighstreet.checkout.dao.ItemsRepository;
import me.olhovsky.notonthehighstreet.checkout.rules.MultiPurchaseDiscountRule;
import me.olhovsky.notonthehighstreet.checkout.rules.PromotionalRule;
import me.olhovsky.notonthehighstreet.checkout.rules.TotalAmountDiscountRule;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;

public class Application {
    private final ItemsRepository itemsRepository;

    public Application(ItemsRepository itemsRepository) {
        this.itemsRepository = itemsRepository;
    }


    public static void main(String[] args) {
        new Application(new ItemsRepository())
                .run();
    }

    public void run() {
        printUsage();
        System.out.print(">");

        Checkout checkout = createCheckout();
        Scanner in = new Scanner(System.in);
        while (in.hasNextLine()) {
            switch (in.next()) {
                case "scan":
                    Optional<Item> item = itemsRepository.getItem(in.next());
                    if (item.isEmpty()) {
                        System.out.println("No such item, sorry.");
                    } else {
                        checkout.scan(item.get());
                        System.out.print("\u0007");
                        System.out.printf("BE-E-E-EP%nAdded item: %s, £%.2f%n", item.get().getName(), item.get().getPrice());
                    }
                    break;
                case "total":
                    System.out.printf("Total: £%.2f%n", checkout.total());
                    break;
                case "clear":
                    checkout = createCheckout();
                    System.out.println("Cleared Checkout");
                    break;
                case "help":
                    printUsage();
                    break;
                case "exit":
                    System.out.println("Goodbye");
                    return;
                default:
                    System.out.println("I don't know how to do this.");
                    break;
            }
            System.out.print(">");
        }

    }

    private Checkout createCheckout() {
        Set<PromotionalRule> rules = Set.of(
                new MultiPurchaseDiscountRule("001", 2, BigDecimal.valueOf(8.5)),
                new TotalAmountDiscountRule(BigDecimal.valueOf(60), BigDecimal.valueOf(10)));
        return new Checkout(rules);
    }

    private void printUsage() {
        System.out.println("notonthehighstreet.com Checkout");
        System.out.println("type \"scan <item code>\" to scan an item");
        System.out.println("type \"total\" to calculate total");
        System.out.println("type \"clear\" to clear Checkout and start over");
        System.out.println("type \"help\" to see this again");
        System.out.println("type \"exit\" when you feel satisfied");
    }
}
