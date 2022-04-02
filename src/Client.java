public class Client {

    public static void main(String[] args) {

        DBHelper db = new DBHelper();
        db.setTrainPercent(70);

        Network network = Network.getInstance();
        // Input - Hidden - Output - Epoch -> size setting
        network.setInputSize(8);
        network.setHiddenSize(16);
        network.setOutputSize(1);
        network.setEpochSize(100);
        network.initialize();

        int size = db.getLength("train");
        System.out.println("Train Page Size : " + size);
        double[][] trainInput = new double[size][network.getInputSize()];
        double[][] trainActual = new double[size][network.getOutputSize()];

        for (int i = 0; i < size; i++) {
            double[] temp = db.getTrainData();
            for (int j = 0; j < 9; j++) {
                if (j < 8) {
                    trainInput[i][j] = temp[j];
                } else {
                    trainActual[i][0] = temp[j];
                }
            }
        }

        size = db.getLength("test");
        System.out.println("Test Page Size : " + size);
        double[][] testInput = new double[size][network.getInputSize()];
        double[][] testActual = new double[size][network.getOutputSize()];

        for (int i = 0; i < size; i++) {
            double[] temp = db.getTestData();
            for (int j = 0; j < 9; j++) {
                if (j < 8) {
                    testInput[i][j] = temp[j];
                } else {
                    testActual[i][0] = temp[j];
                }
            }
        }

        network.calculate(trainInput, trainActual); // Training

        double mapeTest = 0;
        for (int i = 0; i < testInput.length; i++) {
            mapeTest = network.estimate(testInput[i], testActual[i], testInput.length); // Testing
        }
        System.out.println("MAPE (Mean Absolute Percentage Error) Value in Test : " + mapeTest);

        var lineChart = new LineChart(); // Visualization of MSE per iteration
        lineChart.setVisible(true);

    }


}
