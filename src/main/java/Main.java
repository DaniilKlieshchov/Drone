import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Parser parser = new Parser("src/main/resources/data.txt");
        List<EntryData> entryData = parser.parseFile();
        Drone drone = new Drone(entryData);
        System.out.println("Drone started...");

        while (drone.buildingCounter <= entryData.size() - 1) {
            if (drone.position.getY() < Drone.PREDETERMINED_HEIGHT){
                drone.moveUp();
                System.out.printf("Current coordinates: %s \n", drone.position.toString());
                continue;
            }
            Directions direction = drone.scan();
            switch (direction) {
                case FRONT -> {
                    drone.moveUp();
                    drone.previousStep = Directions.FRONT;
                }
                case BACK -> {
                    drone.moveDown();
                    drone.previousStep = Directions.BACK;
                }
                case DOWN -> {
                    drone.moveForward();
                    drone.previousStep = Directions.DOWN;
                }
                case NONE -> {
                    if (drone.previousStep.equals(Directions.BACK)) drone.moveDown();
                    else drone.moveForward();
                    drone.previousStep = Directions.NONE;
                }
            }
            Thread.sleep(200);
            System.out.printf("Current coordinates: %s \n", drone.position.toString());
        }
        for (int i = 0; i < 3; i++){
            drone.forceMoveDown();
            Thread.sleep(200);
            System.out.printf("Current coordinates: %s \n", drone.position.toString());
        }
        System.out.println("Mission completed");
    }

}
