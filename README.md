#### Description
This is a simple checkout application which allows applying different promotion rules to scanned items and calculating
total amount to be paid. It can scan items in any order, and because the promotions will change, it is flexible
regarding promotional rules.

#### Implementation
Approaching the task one observation can be made is that there is a clear business rule about the order in which
promotions should be applied. It appears that one promotion should be applied strictly after another - the total
 spending amount promotion can only be calculated when the total amount is known after applying the multipurchase
  promotion. There are a few approaches that can be used to implement this business rule:

 * It can be implemented directly in `Checkout` class, hard-coding promotion application order. This way however
 a tight coupling between `Checkout` and `PromotionalRule` implementations is created - `Checkout` class needs to "know"
 about concrete rules implementations. This approach is not flexible, as adding new promotional rules would involve
  having to make changes to `Checkout` class.
 
 * Keeping in mind that `Checkout` class is still the one containing promotion application logic which decides 
 the order in which promotions are applied, we can think about a way to make this deterministic without hard-coding
 promotion classes in `Checkout`. We can do this by adding a method to `PromotionalRule` interface which would determine
 the promotion application order. Since in the given scenario we only have two stages when promotions can be applied - 
 before and after calculating total amount - we can use `boolean` and add something like `isOnTotalAmount()` to the
 interface. This will allow us to implement the promotion application order logic in `Checkout` without coupling it
 to concrete `PromotionalRule` implementations.
  
 * However, an even more flexible approach would be to be able to specify the exact order in which promotional are
 applied rather than just having them applied in two groups. This will allow implementing more potential use-cases
 without having to make changes to `Checkout` class. For example a free shipping promotion can be implemented for
 purchases over certain spending amount, which would need to be applied after all other promotions, including multipurchase
 and total amount ones, are calculated. I decided to go with this approach, adding `getPriority()` method to `PromotionalRule`
  interface. Rules are being sorted in `Checkout` class according to value of this field before being applied.

Among other decisions I'm using BigDecimal rather than double or int for monetary amounts as it avoids rounding errors
that are unavoidable with doubles and gives me control over rounding modes. Could've used ints I suppose and have all values in pence 
but I think this is more confusing and would make life of the people dealing with the amounts harder.

I'm adding applied promotional values as special kind of items to a temp list that I pass to the next promotion to be applied.
This allows me to have a uniform interface for all kinds of promotions, whether they are based on the value of previously 
applied promotions or not. With minimal changes this can enable implementation of more sophisticated promotion logic
which can base on the types and values of previously applied promotions. Also I think this can play nicely with how we
display the list of items in checkout to the user - clearly displaying types and values of promotions applied.

#### Potential improvements

* Promotional rule priority can be made a configurable rather than hard-coded parameter. This will add flexibility. 

* Promotional rules parameters can be loaded dynamically from a database instead of being hard-coded.

* Additional methods can be added to `Checkout` to geta list of scanned items together with applicable promotions to display nicely
to a customer.

* How to resolve conflicting promotions? For example if we have two multipurchase promotions for the same item but with
different thresholds. Say we want to set yet even lower price if a customer buys 5 card holders.

* Obviously the implementation is not thread-safe as it is. If concurrent use is a requirement then necessary synchronisations
should be added. 

#### Usage
To run the application *Java 11* or higher is required.

Build the application using:
```
./gradlew clean assemble
```
Run it using
```
java -jar ./build/libs/notonthehighstreet-checkout.jar
```

There is a simple user interface which should be self-explanatory. It allows scanning pre-set items testing how the
application applies promotions and calculates total amount.

