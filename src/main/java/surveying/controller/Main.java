package surveying.controller;

import surveying.drone.Drone;

import java.io.IOException;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Main {
    public static CyclicBarrier barrier = new CyclicBarrier(2);
    public static void main(final String[] args) throws IOException, InterruptedException, BrokenBarrierException {

        final DroneController menu = new DroneController();
        final Drone drone = new Drone();
        Operation chose;
        do {
            chose = Operation.fromId(menu.mainMenu(drone));
        } while (chose != Operation.EXIT);
    }
}
