public class Utils {

    public static void sigmoid(Unit unit) {
        for (int i = 0; i < unit.rows; i++) {
            for (int j = 0; j < unit.cols; j++) {
                unit.data[i][j] = 1 / (1 + Math.exp(-unit.data[i][j])); // Sigmoid Formula ->  1 / 1 + e^(-x)
            }
        }
    }

    public static Unit derivatizedSigmoid(Unit unit) {
        Unit res = new Unit(unit.rows, unit.cols);
        for (int i = 0; i < unit.rows; i++) {
            for (int j = 0; j < unit.cols; j++) {
                res.data[i][j] = unit.data[i][j] * (1 - unit.data[i][j]); // Derivatized form -> x * (1-x)
            }
        }
        return res;
    }

    public static Unit fromArray(double[] values) {
        Unit res = new Unit(values.length, 1);
        for (int i = 0; i < values.length; i++) {
            res.data[i][0] = values[i];
        }
        return res;
    }

    public static void add(Unit u1, Unit u2) {
        for (int i = 0; i < u1.rows; i++) {
            for (int j = 0; j < u1.cols; j++) {
                u1.data[i][j] += u2.data[i][j];
            }
        }
    }

    public static Unit subtract(Unit u1, Unit u2) {
        Unit res = new Unit(u1.rows, u1.cols);
        for (int i = 0; i < u1.rows; i++) {
            for (int j = 0; j < u1.cols; j++) {
                res.data[i][j] = u1.data[i][j] - u2.data[i][j];
            }
        }
        return res;
    }

    public static Unit reverse(Unit unit) {
        Unit reversed = new Unit(unit.cols, unit.rows);
        for (int i = 0; i < unit.rows; i++) {
            for (int j = 0; j < unit.cols; j++) {
                reversed.data[j][i] = unit.data[i][j]; // Changing axis
            }
        }
        return reversed;
    }

    public static Unit multiply(Unit u1, Unit u2) {
        Unit res = new Unit(u1.rows, u2.cols);
        for (int i = 0; i < res.rows; i++) {
            for (int j = 0; j < res.cols; j++) {
                double sum = 0;
                for (int k = 0; k < u1.cols; k++) {
                    sum += u1.data[i][k] * u2.data[k][j]; // Multiplying first unit's row and second unit's column
                }
                res.data[i][j] = sum;
            }
        }
        return res;
    }

    public static void multiplyNormal(Unit u1, Unit u2) {
        for (int i = 0; i < u2.rows; i++) {
            for (int j = 0; j < u2.cols; j++) {
                u1.data[i][j] *= u2.data[i][j];
            }
        }
    }

    public static void multiplyWithConst(Unit unit, double value) {
        for (int i = 0; i < unit.rows; i++) {
            for (int j = 0; j < unit.cols; j++) {
                unit.data[i][j] *= value;
            }
        }
    }

}
