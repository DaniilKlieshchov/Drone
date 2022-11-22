package surveying.controller;

public enum Operation {
    AGAIN(0),
    ROUTE(1),
    BATTERY(2),
    STATUS(3),
    EXIT(4),
    PAUSE(5),
    RESET(6),
    ABORT(7),
    CHECK_PROGRESS(8),
    GET_LOCATION(9),
    NOTHING(10);


    public int getId() {
        return id;
    }

    private final int id;

    Operation(int id) {
        this.id = id;
    }

    public static Operation fromId(int id) {
        for (Operation type : values()) {
            if (type.getId() == id) {
                return type;
            }
        }
        return null;
    }
}
