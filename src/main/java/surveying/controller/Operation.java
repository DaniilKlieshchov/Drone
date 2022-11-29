package surveying.controller;

public enum Operation {


    AGAIN(0),
    ROUTE(1),
    BATTERY(2),
    STATUS(3),
    GET_LOCATION(4),
    PAUSE(5),
    RESET(6),
    ABORT(7),
    CHECK_PROGRESS(8),
    EXIT(9),
    NOTHING(10);
    private final int id;

    public int getId() {
        return id;
    }

    Operation(final int id) {
        this.id = id;
    }

    public static Operation fromId(final int id) {
        for (final Operation type : values()) {
            if (type.getId() == id) {
                return type;
            }
        }
        return null;
    }
}
