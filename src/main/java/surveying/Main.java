package surveying;

import surveying.drone.Drone;
import surveying.menu.Menu;
import surveying.menu.Operation;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {

        Menu menu = new Menu();
        Drone drone = new Drone();
        Operation chose;
        do{
            chose = Operation.fromId(menu.mainMenu(drone));
        } while (chose != Operation.EXIT);
    }
}
