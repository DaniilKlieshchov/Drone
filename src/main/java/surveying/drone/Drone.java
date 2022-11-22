package surveying.drone;

import surveying.controller.Operation;
import surveying.controller.utilities.EntryData;

import java.util.*;
import java.util.concurrent.BrokenBarrierException;

import static java.lang.Thread.sleep;


public class Drone {
    public int buildingCounter;
    public Directions previousStep = Directions.NONE;
    public List<EntryData> inputData;
    public Position position = new Position(0, 0);
    public Map<Directions, Position> vision;
    static public int PREDETERMINED_HEIGHT = 3;
    public DroneStatus status = DroneStatus.READY;
    private Operation operation;
    private double battery = 100.0;

    public boolean isPaused() {
        return isPaused;
    }

    private boolean isPaused;

    public double getBattery() {
        return battery;
    }

    public void recordInput(final List<EntryData> inputData) {
        this.vision = new HashMap<>();
        this.vision.put(Directions.FRONT, new Position());
        this.vision.put(Directions.BACK, new Position());
        this.vision.put(Directions.UP, new Position());
        this.vision.put(Directions.DOWN, new Position());
        this.inputData = inputData;
    }

    public void clearData() {
        this.vision = new HashMap<>();
        this.vision.put(Directions.FRONT, new Position());
        this.vision.put(Directions.BACK, new Position());
        this.vision.put(Directions.UP, new Position());
        this.vision.put(Directions.DOWN, new Position());
        this.position = new Position();
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
        if (position.getY() > PREDETERMINED_HEIGHT) {
            position.setY(position.y - 1);
            updateVisionPosition();
        } else {
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
        final int yBuilding = inputData.get(buildingCounter).Height();
        final int xBuildingFirstWall = inputData.get(buildingCounter).xCoordinate();
        final int xBuildingSecondWall = inputData.get(buildingCounter).xCoordinate() + inputData.get(buildingCounter).Width();
        if (vision.get(Directions.FRONT).getX() == xBuildingFirstWall && vision.get(Directions.DOWN).getY() < yBuilding) {
            {
                return Directions.FRONT;
            }
        } else if (vision.get(Directions.DOWN).getY() == yBuilding && vision.get(Directions.BACK).getX() < xBuildingSecondWall) {
            {
                return Directions.DOWN;
            }
        } else if (vision.get(Directions.BACK).getX() == xBuildingSecondWall) {
            {
                return Directions.BACK;
            }
        } else {
            return Directions.NONE;
        }
    }

    public void survey(final List<EntryData> entryData) throws InterruptedException{
        System.out.println("Surveying started...");
        status = DroneStatus.SURVEYING;
        while (buildingCounter <= entryData.size() - 1) {
            try {
                if(Optional.ofNullable(operation).isPresent()){
                    this.executeOperation(operation);
                }
            }
            catch (BrokenBarrierException brokenBarrierException) {
                System.out.println();
            }
            if (isPaused) {
                continue;
            }
            if (position.getY() < Drone.PREDETERMINED_HEIGHT) {
                moveUp();
                //System.out.printf("Current coordinates: %s \n", position.toString());
                continue;
            }
            final Directions direction = scan();
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
                    if (previousStep.equals(Directions.BACK)) {
                        moveDown();
                    }
                    else {
                        moveForward();
                    }
                    previousStep = Directions.NONE;
                }
            }
            sleep(150);
            //System.out.printf("Current coordinates: %s \n", position.toString());

            battery -= 0.1;
            if (battery <= 1) {
                System.out.println("Battery is dead");
                return;
            }
        }
        for (int i = 0; i < 3; i++) {
            forceMoveDown();
            sleep(100);
            System.out.printf("Current coordinates: %s \n", position.toString());
        }
        System.out.println("Mission completed");
        status = DroneStatus.READY;

    }

    public void setOperation(final Operation operation) {
        this.operation = operation;
    }

    public void executeOperation(final Operation operation) throws BrokenBarrierException, InterruptedException {
//        Main.barrier.await();
        switch (Objects.requireNonNull(operation)) {
            case BATTERY -> System.out.println("The battery level: " + (int) this.getBattery() + "%");
            case STATUS -> System.out.println("The drone status: " + this.status);
            case PAUSE -> {
                System.out.println("Drone paused");
                isPaused = true;
            }
            case RESET -> {
                System.out.println("Drone in process...");
                isPaused = false;
            }
            case ABORT -> {
                System.out.println("Mission aborted");
                clearData();
            }
            case CHECK_PROGRESS -> {
                System.out.println(getProgress());
            }
            case EXIT -> {
                if (getProgress() == 100.0) {
                    System.out.println("Good bye");
                }
                else {
                    System.out.println("Operation is not available");
                }
            }
            case GET_LOCATION -> System.out.println(this.position);
            case ROUTE -> System.out.println("");
            default -> System.out.println("operation is not available");
        }
        this.operation = null;
    }

//    public void pause() throws BrokenBarrierException, InterruptedException {
//        Main.barrier.await();
//    }
    public double getProgress(){
        return (double) position.x / (inputData.get(inputData.size() - 1).xCoordinate() + 3) * 100;
    }



}

