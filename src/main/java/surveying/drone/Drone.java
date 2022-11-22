package surveying.drone;

import surveying.EntryData;
import surveying.menu.Operation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.lang.Thread.sleep;


public class Drone {
    public int buildingCounter;
    public Directions previousStep = Directions.NONE;
    public List<EntryData> inputData;
    public Position position = new Position(0, 0);
    private Map<Directions, Position> vision;
    static public int PREDETERMINED_HEIGHT = 3;
    public DroneStatus status = DroneStatus.READY;
    private double battery = 100.0;
    private Operation command;
    public double getBattery() {
        return battery;
    }

    public void recordInput(List<EntryData> inputData) {
        this.vision = new HashMap<>();
        this.vision.put(Directions.FRONT, new Position());
        this.vision.put(Directions.BACK, new Position());
        this.vision.put(Directions.UP, new Position());
        this.vision.put(Directions.DOWN, new Position());
        this.inputData = inputData;
    }

    public void moveForward() {
        position.setX(position.x + 1);
        updateVisionPosition();
    }


    public void moveUp() {
        position.setY(position.y + 1);
        updateVisionPosition();
    }

    public void moveDown() {
        if(position.getY() > PREDETERMINED_HEIGHT) {
            position.setY(position.y - 1);
            updateVisionPosition();
        }
        else{
            buildingCounter++;
        }
    }
    public void forceMoveDown() {
        position.setY(position.y - 1);
        updateVisionPosition();
    }

    private void updateVisionPosition() {
        vision.get(Directions.FRONT).setX(position.getX() + 3);
        vision.get(Directions.FRONT).setY(position.getY());
        vision.get(Directions.BACK).setX(position.getX() - 3);
        vision.get(Directions.BACK).setY(position.getY());
        vision.get(Directions.UP).setX(position.getX());
        vision.get(Directions.UP).setY(position.getY() + 3);
        vision.get(Directions.DOWN).setX(position.getX());
        vision.get(Directions.DOWN).setY(position.getY() - 3);
    }

    public Directions scan() {
        int yBuilding = inputData.get(buildingCounter).Height();
        int xBuildingFirstWall = inputData.get(buildingCounter).xCoordinate();
        int xBuildingSecondWall = inputData.get(buildingCounter).xCoordinate() + inputData.get(buildingCounter).Width();
        if (vision.get(Directions.FRONT).getX() == xBuildingFirstWall && vision.get(Directions.DOWN).getY() < yBuilding) {
            return Directions.FRONT;
        } else if (vision.get(Directions.DOWN).getY() == yBuilding && vision.get(Directions.BACK).getX() < xBuildingSecondWall) {
            return Directions.DOWN;
        } else if (vision.get(Directions.BACK).getX() == xBuildingSecondWall) {
            return Directions.BACK;
        } else return Directions.NONE;
    }

    public void survey(List<EntryData> entryData) throws InterruptedException{
        System.out.println("Surveying started...");
        status = DroneStatus.SURVEYING;
        while (buildingCounter <= entryData.size() - 1) {
            if (position.getY() < Drone.PREDETERMINED_HEIGHT){
                moveUp();
                System.out.printf("Current coordinates: %s \n", position.toString());
                continue;
            }
            Directions direction = scan();
            switch (direction) {
                case FRONT -> {
                    moveUp();
                    previousStep = Directions.FRONT;
                }
                case BACK -> {
                    moveDown();
                    previousStep = Directions.BACK;
                }
                case DOWN -> {
                    moveForward();
                    previousStep = Directions.DOWN;
                }
                case NONE -> {
                    if (previousStep.equals(Directions.BACK)) moveDown();
                    else moveForward();
                    previousStep = Directions.NONE;
                }
            }
            sleep(100);
            System.out.printf("Current coordinates: %s \n", position.toString());

            battery -= 0.1;
            if (battery <= 1) {
                System.out.println("Battery is dead");
                return;
            }
        }
        for (int i = 0; i < 3; i++){
            forceMoveDown();
            sleep(100);
            System.out.printf("Current coordinates: %s \n", position.toString());
        }
        System.out.println("Mission completed");
        status = DroneStatus.READY;
    }

    public void setCommand(Operation command) {
        this.command = command;
    }
}

