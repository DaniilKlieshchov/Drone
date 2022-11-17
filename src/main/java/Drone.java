import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Drone {
    int buildingCounter;
    Directions previousStep = Directions.NONE;
    List<EntryData> inputData;
    Position position = new Position(0, 0);
    Map<Directions, Position> vision;
    static public int PREDETERMINED_HEIGHT = 3;


    public Drone(List<EntryData> inputData) {
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

}

