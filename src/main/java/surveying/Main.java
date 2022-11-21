package surveying;

import surveying.drone.Drone;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {

        Menu menu = new Menu();

        Parser parser = new Parser("src/main/resources/" + menu.chooseRoute());
        List<EntryData> entryData = parser.parseFile();
        Drone drone = new Drone(entryData);
        drone.survey(entryData);


        System.out.println((int)drone.getBattery() + "%");
    }
}
