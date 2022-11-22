package surveying.drone;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Position {
    /* default */ int x;
    /* default */ int y;

    public Position(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    public Position() {
        this.x = 0;
        this.y = 0;
    }

    public int getX() {
        return x;
    }

    public void setX(final int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(final int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "(" + x + "; " + y + ")";

    }
}


