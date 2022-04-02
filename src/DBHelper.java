import java.io.*;
import java.util.Random;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class DBHelper {

    // HSSF 1997-2007 | xls type  | 65536 rows
    // XSSF 2007+     | xlsx type | 1.048.576 rows

    static Normalization norm;
    File file;
    XSSFSheet pageNormalized;
    XSSFSheet pageOne;
    XSSFSheet pageTwo;
    String sourceName = "dataset.xlsx";
    private double[][] dataAll;
    private double[][] dataRaw;
    private Random rand;
    private int trainPercent;


    public DBHelper() {
        // TODO: if normalized.xlsx exist fetch data directly without normalization

        pageOne = getPage(0);// first page returned
        pageTwo = getPage(1);// second page returned
        dataRaw = getDataRaw(pageOne, pageTwo); // Merging pages
        norm = new Normalization(getMaxValue(), getMinValue(), 10, 0); // dataRaw must be ready
        setDataNormalized(); // norm must be ready
        dataAll = getDataNormalized(); // setDataNormalize func. -> pageNormalized  must be ready
        rand = new Random();
    }

    public double[][] getDataRaw(XSSFSheet pageOne, XSSFSheet pageTwo) {
        int totalRow = (pageOne.getPhysicalNumberOfRows()) + (pageTwo.getPhysicalNumberOfRows()) - 2;
        double[][] allData = new double[totalRow][pageOne.getRow(0).getPhysicalNumberOfCells()];
        int i;
        for (i = 1; i < pageOne.getPhysicalNumberOfRows(); i++) {
            for (int j = 0; j < pageOne.getRow(i).getPhysicalNumberOfCells(); j++) {
                allData[i - 1][j] = pageOne.getRow(i).getCell(j).getNumericCellValue();
            }
        }
        i--;
        for (int j = 1; j < pageTwo.getPhysicalNumberOfRows(); j++) {
            for (int k = 0; k < pageTwo.getRow(j).getPhysicalNumberOfCells(); k++) {
                allData[i][k] = pageTwo.getRow(j).getCell(k).getNumericCellValue();
            }
            i++;
        }

        return allData;
    }

    public void setDataNormalized() {
        XSSFWorkbook workbook = new XSSFWorkbook();
        pageNormalized = workbook.createSheet("normalized-dataset");
        Row row;
        Cell cell;

        for (int i = 0; i < dataRaw.length; i++) {
            row = pageNormalized.createRow(i);
            for (int j = 0; j < dataRaw[0].length; j++) {
                cell = row.createCell(j);
                cell.setCellValue(norm.normalize(dataRaw[i][j]));
            }
        }

        try (FileOutputStream outputStream = new FileOutputStream("normalized.xlsx")) {
            workbook.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public XSSFSheet getPage(int pageInd) {
        FileInputStream stream = null;
        XSSFWorkbook workbook = null;
        file = new File(getClass().getResource(sourceName).getFile());
        try {
            stream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            workbook = new XSSFWorkbook(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return workbook.getSheetAt(pageInd);
    }

    public double[][] getDataNormalized() {
        double[][] dataAll = new double[pageNormalized.getPhysicalNumberOfRows()][pageNormalized.getRow(0).getPhysicalNumberOfCells()];
        for (int i = 0; i < pageNormalized.getPhysicalNumberOfRows(); i++) {
            for (int j = 0; j < pageNormalized.getRow(i).getPhysicalNumberOfCells(); j++) {
                dataAll[i][j] = pageNormalized.getRow(i).getCell(j).getNumericCellValue();
            }
        }

        return dataAll;
    }


    public double[] getTrainData() {
        double[][] currentData = dataAll;
        int lastInd = currentData.length * getTrainPercent() / 100;
        int randomValue = rand.nextInt(lastInd);
        double[] currentRow = new double[currentData[0].length];
        for (int index = 0; index < currentData[0].length; index++) {
            currentRow[index] = currentData[randomValue][index];
        }
        return currentRow;
    }

    public double[] getTestData() {
        double[][] currentData = dataAll;
        int startInd = currentData.length * getTrainPercent() / 100;
        int randomValue = startInd + rand.nextInt(currentData.length - startInd);

        double[] currentRow = new double[currentData[0].length];
        for (int index = 0; index < currentData[0].length; index++) {
            currentRow[index] = currentData[randomValue][index];
        }
        return currentRow;
    }

    public int getLength(String dataType) {
        double[][] currentData = dataAll;
        if (dataType.equals("train")) {
            return currentData.length * getTrainPercent() / 100;
        } else if (dataType.equals("test")) {
            return currentData.length * (100 - getTrainPercent()) / 100;
        } else {
            return currentData.length;
        }
    }

    public double getMaxValue() {
        double[][] currentData = dataRaw;
        int lastInd = currentData.length * 70 / 100;
        double maxValue = Double.MIN_VALUE;

        for (int i = 0; i < lastInd; i++) {
            for (int j = 0; j < currentData[0].length - 1; j++) {
                double temp = currentData[i][j];
                if (temp > maxValue) {
                    maxValue = temp;
                }
            }
        }
        return maxValue;
    }

    public double getMinValue() {
        double[][] currentData = dataRaw;
        int lastInd = currentData.length * 70 / 100;
        double minValue = Double.MAX_VALUE;

        for (int i = 0; i < lastInd; i++) {
            for (int j = 0; j < currentData[0].length; j++) {
                double temp = currentData[i][j];
                if (temp < minValue) {
                    minValue = temp;
                }
            }
        }
        return minValue;
    }


    public int getTrainPercent() {
        return trainPercent;
    }

    public void setTrainPercent(int trainPercent) {
        this.trainPercent = trainPercent;
    }

}
