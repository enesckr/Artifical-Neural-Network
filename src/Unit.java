class Unit {

    static double mapeSum = 0, mseSum = 0;
    static double mape = 0, mse = 0; // Each epoch has different mse and mape value

    double[][] data;
    int rows, cols;

    public Unit(int rows, int cols) {
        data = new double[rows][cols];
        this.rows = rows;
        this.cols = cols;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[i][j] = Math.random(); // take between 0 - 1
            }
        }
    }

    public static double calcMAPE(double tempError, Unit actual, int n) {
        double deActual = DBHelper.norm.denormalize(actual.data[0][0]);
        tempError = DBHelper.norm.denormalize(tempError);

        if (deActual != 0) {
            tempError = Math.abs(tempError / (deActual));
        }
        mapeSum += tempError;
        mape = (100 / (float) n) * mapeSum;
        return mape;
    }

    public static double calcMSE(double tempError, int n) {
        tempError = Math.pow(tempError, 2);
        mseSum += tempError;
        mse = mseSum / (float) n;
        return mse;
    }

}
