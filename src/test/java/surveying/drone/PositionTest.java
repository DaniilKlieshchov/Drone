package surveying.drone;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PositionTest {

    Position position = new Position();

    @Test
    void getX() {
        assertEquals(0, position.getX());
    }

    @Test
    void setX() {
        position.setX(5);
        assertEquals(5, position.getX());
    }

    @Test
    void getY() {
        assertEquals(0, position.getY());
    }

    @Test
    void setY() {
        position.setY(10);
        assertEquals(10, position.getY());
    }

    @Test
    void testToString() {
        assertEquals("(0; 0)", position.toString());
    }
}