package surveying.menu;

public enum Operation {
    AGAIN(0),
    ROUTE(1),
    BATTERY(2),
    EXIT(3);


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
