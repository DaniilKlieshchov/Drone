package surveying.drone;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import surveying.controller.Operation;
import surveying.controller.utilities.EntryData;
import surveying.controller.utilities.Parser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;

import static org.junit.jupiter.api.Assertions.*;

class DroneTest {

    Drone drone = new Drone();
    Parser parser = new Parser("src/main/resources/testroute.txt");
    List<EntryData> entryData = parser.parseFile();
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @BeforeEach
    void record(){
        drone.recordInput(entryData);
    }

    DroneTest() throws IOException {
    }

    @Test
    void getBattery() {
        assertEquals(100.0, drone.getBattery());
    }

    @Test
    void clearData() {
        drone.clearData();
    }

    @Test
    void moveForward() {
        drone.moveForward();
    }

    @Test
    void moveUp() {
        drone.moveUp();
    }

    @Test
    void moveDown() {
        drone.moveDown();
    }

    @Test
    void moveDownHighPosition() {
        drone.position.setY(5);
        drone.moveDown();
        assertEquals(4, drone.position.getY());
    }

    @Test
    void forceMoveDown() {
        drone.forceMoveDown();
    }

    @Test
    void scan() {
        drone.scan();
    }

    @Test
    void survey() throws InterruptedException {
        drone.survey(entryData);
    }

    @Test
    void setOperation() {
        drone.setOperation(Operation.AGAIN);
        assertEquals(Operation.fromId(0), drone.getOperation());
    }

    @Test
    void executeOperationPause() throws BrokenBarrierException, InterruptedException {
        drone.executeOperation(Operation.PAUSE);
        assertTrue(drone.isPaused());
    }

    @Test
    void executeOperationReset() throws BrokenBarrierException, InterruptedException {
        drone.executeOperation(Operation.RESET);
        assertFalse(drone.isPaused());
    }

    @Test
    void executeOperationAbort() throws BrokenBarrierException, InterruptedException {
        drone.executeOperation(Operation.ABORT);
        assertEquals("Mission aborted",  outputStreamCaptor.toString().trim());
    }

    @Test
    void executeOperationBattery() throws BrokenBarrierException, InterruptedException {
        drone.executeOperation(Operation.BATTERY);
        assertEquals("The battery level: 100%",  outputStreamCaptor.toString().trim());
    }

    @Test
    void executeOperationStatus() throws BrokenBarrierException, InterruptedException {
        drone.executeOperation(Operation.STATUS);
        assertEquals("The drone status: " + DroneStatus.READY.toString(),  outputStreamCaptor.toString().trim());
    }

    @Test
    void executeOperationProgress() throws BrokenBarrierException, InterruptedException {
        drone.executeOperation(Operation.CHECK_PROGRESS);
        assertEquals("0.0",  outputStreamCaptor.toString().trim());
    }

    @Test
    void executeOperationExit() throws BrokenBarrierException, InterruptedException {
        drone.executeOperation(Operation.EXIT);
        assertEquals("Operation is not available",  outputStreamCaptor.toString().trim());
    }

    @Test
    void executeOperationLocation() throws BrokenBarrierException, InterruptedException {
        drone.executeOperation(Operation.GET_LOCATION);
        assertEquals("(0; 0)",  outputStreamCaptor.toString().trim());
    }

    @Test
    void executeOperationRoute() throws BrokenBarrierException, InterruptedException {
        drone.executeOperation(Operation.ROUTE);
        assertEquals("",  outputStreamCaptor.toString().trim());
    }

    @Test
    void executeOperationDefault() throws BrokenBarrierException, InterruptedException {
        drone.executeOperation(Operation.AGAIN);
        assertEquals("operation is not available",  outputStreamCaptor.toString().trim());
    }

    @Test
    void getProgress() {
        assertEquals(0.0, drone.getProgress());
    }
}