import org.jfree.data.xy.XYSeries;

public class Network {

    private static Network network;
    int inputSize, hiddenSize, outputSize, epochSize;
    Unit ihWeight, hoWeight, hiddenBias, outputBias;
    double learningRate = 0.01, momentumFactor = 0.02;
    double hoWeightPrev = 0, ihWeightPrev = 0; // Hold previous weights for new neurons with momentum factor
    double afterEpochMAPE = Double.MAX_VALUE;
    double afterEpochMSE = Double.MAX_VALUE;
    Boolean hasFinished = false;
    XYSeries series;

    private Network() {
    }

    public static Network getInstance() {
        if (network == null) {
            network = new Network();
        }
        return network;
    }

    public void initialize() {
        series = new XYSeries("value"); // key -> description for red dots
        ihWeight = new Unit(getHiddenSize(), getInputSize());
        hoWeight = new Unit(getOutputSize(), getHiddenSize());
        hiddenBias = new Unit(getHiddenSize(), 1);
        outputBias = new Unit(getOutputSize(), 1);
    }

    public void calculate(double[][] X, double[][] Y) {
        for (int i = 0; i < getEpochSize(); i++) {
            for (int j = 0; j < X.length; j++) {
                int sampleN = (int) (Math.random() * X.length);
                this.train(X[sampleN], Y[sampleN], Y.length);
                if (hasFinished) {
                    return;
                }
            }
            afterEpochMAPE = Unit.mape;
            System.out.println(i + ". epoch's MAPE value -> " + afterEpochMAPE);
            Unit.mapeSum = 0; // Reset mape summary for new epoch

            afterEpochMSE = Unit.mse;// assign for visualization
            Unit.mseSum = 0;

            series.add(i, afterEpochMSE); // x and y axis values adding

        }
    }

    public void train(double[] X, double[] Y, int n) {

        // ------------ Forward propagation -------------
        Unit inputLayer = Utils.fromArray(X);

        // (x1*w1 + x2*x2 + ...) + bias  ---> sigmoid()
        Unit hiddenLayer = Utils.multiply(ihWeight, inputLayer);
        Utils.add(hiddenLayer, hiddenBias);
        Utils.sigmoid(hiddenLayer);

        // (x1*w1 + x2*x2 + ...) + bias  ---> sigmoid()
        Unit outputLayer = Utils.multiply(hoWeight, hiddenLayer);
        Utils.add(outputLayer, outputBias);
        Utils.sigmoid(outputLayer);

        Unit actual = Utils.fromArray(Y);
        Unit error = Utils.subtract(actual, outputLayer); // actual - output = error
        double tempError = error.data[0][0];
        Unit.calcMSE(tempError, n);
        Unit.calcMAPE(tempError, actual, n);

        if (afterEpochMAPE <= 3) {
            hasFinished = true;
            return;
        }

        // ------------ Backward propagation -------------
        Unit slope = Utils.derivatizedSigmoid(outputLayer);
        Utils.multiplyNormal(slope, error);
        Utils.multiplyWithConst(slope, learningRate);

        Unit hiddenReversed = Utils.reverse(hiddenLayer);
        Unit who_delta = Utils.multiply(slope, hiddenReversed);

        // previous value returned 0 in first step
        who_delta.data[0][0] += momentumFactor * hoWeightPrev; // Implementing formula the momentum factor to algorithm
        Utils.add(hoWeight, who_delta);
        hoWeightPrev = who_delta.data[0][0];
        Utils.add(outputBias, slope);

        Unit whoReversed = Utils.reverse(hoWeight);
        Unit hidden_errors = Utils.multiply(whoReversed, error);

        Unit hiddenD = Utils.derivatizedSigmoid(hiddenLayer);
        Utils.multiplyNormal(hiddenD, hidden_errors);
        Utils.multiplyWithConst(hiddenD, learningRate);

        Unit inputReversed = Utils.reverse(inputLayer);
        Unit wihD = Utils.multiply(hiddenD, inputReversed);

        // previous value returned 0 in first step
        wihD.data[0][0] += momentumFactor * ihWeightPrev; // Implementing formula the momentum factor to algorithm
        Utils.add(ihWeight, wihD);
        ihWeightPrev = wihD.data[0][0];
        Utils.add(hiddenBias, hiddenD);
    }

    public Double estimate(double[] X, double[] Y, int n) {

        Unit inputLayer = Utils.fromArray(X);

        // (x1*w1 + x2*x2 + ...) + bias  ---> sigmoid()
        Unit hiddenLayer = Utils.multiply(ihWeight, inputLayer);
        Utils.add(hiddenLayer, hiddenBias);
        Utils.sigmoid(hiddenLayer);

        // (x1*w1 + x2*x2 + ...) + bias  ---> sigmoid()
        Unit outputLayer = Utils.multiply(hoWeight, hiddenLayer);
        Utils.add(outputLayer, outputBias);
        Utils.sigmoid(outputLayer);

        Unit actual = Utils.fromArray(Y);
        Unit error = Utils.subtract(actual, outputLayer);

        double tempError = error.data[0][0];
        return Unit.calcMAPE(tempError, actual, n);
    }

    public int getInputSize() {
        return inputSize;
    }

    public void setInputSize(int inputSize) {
        this.inputSize = inputSize;
    }

    public int getHiddenSize() {
        return hiddenSize;
    }

    public void setHiddenSize(int hiddenSize) {
        this.hiddenSize = hiddenSize;
    }

    public int getOutputSize() {
        return outputSize;
    }

    public void setOutputSize(int outputSize) {
        this.outputSize = outputSize;
    }

    public int getEpochSize() {
        return epochSize;
    }

    public void setEpochSize(int epochSize) {
        this.epochSize = epochSize;
    }

}
