package im;

import lombok.Data;

/**
 * @author : ybyao
 * @Create : 2019-12-17 15:11
 */
@Data
public class Point {
    int x;
    int y;
    double data;

    public Point(int x, int y, double data) {
        this.x = x;
        this.y = y;
        this.data = data;
    }
}
