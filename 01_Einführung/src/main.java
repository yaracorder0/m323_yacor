import java.util.ArrayList;
import java.util.List;

public class main {
    public static void main(String[] args){
        // Aufgabe 1
        System.out.println("Imperative Ergebnisse");
        System.out.println("calculateScore(\"imperative\") == " + Score_1.calculateScore("imperative"));
        System.out.println("calculateScore(\"no\") = " + Score_1.calculateScore("no"));

        System.out.println("\nDeklarative Ergebnisse");
        System.out.println("wordScore(\"declarative\") == " + Score_1.calculateScore("declarative"));
        System.out.println("wordScore(\"yes\") == " + Score_1.calculateScore("yes"));


        // Aufgabe 2
        System.out.println("\nShopping Cart");

        ShoppingCart_2 cart = new ShoppingCart_2();

        cart.addItem("Java Handbuch");
        cart.addItem("Tasche");
        System.out.println("Items: " + cart.getItems());
        System.out.println("Rabatt: " + cart.getDiscountPercentage());

        cart.deleteItem("Java Handbuch");
        System.out.println("Rabatt: " + cart.getDiscountPercentage());

        System.out.println("\nFunktionale Ergebnisse");
        List<String> functionalCart = new ArrayList<>();
        functionalCart.add("Brot");
        functionalCart.add("Clean Code (Book)");
        double discount = DiscountCalculator_2.getDiscountPercentage(functionalCart);
        System.out.println("Items: " + functionalCart);
        System.out.println("Rabatt (erwartet 0.05): " + discount);

        functionalCart.remove("Clean Code (Book)");
        discount = DiscountCalculator_2.getDiscountPercentage(functionalCart);
        System.out.println("Nach Löschen - Rabatt (erwartet 0.0): " + discount);


        // Aufgabe 3
        System.out.println("\nPure Function (TipCalc)");
        List<String> names = new ArrayList<>();
        names.add("Fritz");
        names.add("Franz");
        names.add("Hugo");
        names.add("Erwin");
        names.add("Markus");
        names.add("Martin");
        names.add("Heiri");

        int tip = TipCalculator_3.getTipPercentage(names);
        System.out.println("Gruppe " + names);
        System.out.println("Tip percentage " + tip + " %");
    }
}