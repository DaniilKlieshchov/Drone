package surveying.controller;

import surveying.drone.Drone;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {

        DroneController menu = new DroneController();
        Drone drone = new Drone();
        Operation chose;
        do {
            chose = Operation.fromId(menu.mainMenu(drone));
        } while (chose != Operation.EXIT);
    }
}
