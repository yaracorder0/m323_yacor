import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("Reise Planen");
        List<String> route = new ArrayList<>();
        route = Reise.addDestination(route, "Paris");
        route = Reise.addDestination(route, "Rom");
        route = Reise.addDestination(route, "Berlin");

        System.out.println("Nach addDestination:");
        System.out.println(route);

        route = Reise.changeDestination(route, 1, "Madrid");

        System.out.println("Nach changeDestination:");
        System.out.println(route);

        route = Reise.removeDestination(route, 0);

        System.out.println("Nach removeDestination:");
        System.out.println(route);

        System.out.println("Aktuelle Route:");
        System.out.println(Reise.getRoute(route));

    }
}