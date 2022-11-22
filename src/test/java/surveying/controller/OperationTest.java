package surveying.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OperationTest {

    @Test
    void getId() {
        assertEquals(0, Operation.AGAIN.getId());
    }

    @Test
    void fromId() {
        assertEquals(Operation.AGAIN, Operation.fromId(0));
        assertNull(Operation.fromId(-4));
    }


}