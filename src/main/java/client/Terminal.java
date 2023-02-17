package client;

import data.MatrixData;
import data.MatrixSolver;
import exceptions.WrongDataException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;
import java.util.Scanner;

public class Terminal {

    private Scanner scanner = new Scanner(System.in);
    private boolean isFromFile = false;

    public void start() {
        System.out.println("Choose input format (keyboard or file) k/f:");
        isFromFile = isFileFormat();
        if (!isFromFile)
            System.out.println("Input matrix dimension and accuracy (epsilon) by semicolon.\nEx: {dimension}; {epsilon}");
        MatrixData matrixData = getMatrixParams();
        if (!isFromFile)
            System.out.println("\nInput your factors by line by semicolon.\nEx: {a_11}; {a_22}; ...; {a_1n}; {b_1}");
        getFactors(matrixData);
        System.out.println("\nYour matrix: " + matrixData);
        MatrixSolver matrixSolver = new MatrixSolver(matrixData);
        matrixSolver.solve();
    }

    public void refresh() {
        isFromFile = false;
        scanner = new Scanner(System.in);
        System.clearProperty("fileName");
    }

    private MatrixData getMatrixParams() {
        if (!isFromFile) System.out.print(">");
        String[] params = scanner.nextLine().trim().split(";");
        if (params.length != 2) {
            System.out.println("Wrong format!");
            if (isFromFile) System.exit(-1);
            return getMatrixParams();
        }
        String dimension = params[0];
        String epsilon = params[1];
        try {
            return new MatrixData(dimension, epsilon);
        } catch (WrongDataException exception) {
            System.out.println(exception.getMessage());
            if (isFromFile) System.exit(-1);
        }
        return getMatrixParams();
    }

    private boolean isFileFormat() {
        System.out.print(">");
        String format = scanner.nextLine();
        if (Objects.equals(format, "f")) {
            scanner = getScannerFromFile();
            return true;
        } else if (Objects.equals(format, "k")) {
            return false;
        } else {
            System.out.println("k/f");
            return isFileFormat();
        }
    }

    private Scanner getScannerFromFile() {
        String fileName = System.getProperty("fileName");
        if (fileName == null) {
            System.out.println("Text name of the input file:");
            System.out.print(">fileName = ");
            fileName = scanner.nextLine();
            System.setProperty("fileName", fileName);
        }
        try {
            return new Scanner(new File(fileName));
        } catch (FileNotFoundException e) {
            System.out.println("File with filename '" + fileName + "' not found. Try again.");
            System.clearProperty("fileName");
        }
        return getScannerFromFile();
    }

    private void getFactors(MatrixData matrixData) {
        int dimension = matrixData.getDIMENSION();
        int exDimension = matrixData.getEXTENDED_DIMENSION();
        double[][] matrix = new double[dimension][exDimension];
        for (int i = 0; i < dimension; i++) {
            double[] row = getMatrixRow(exDimension, i);
            matrix[i] = row;
        }
        matrixData.setMatrix(matrix);
    }

    private double[] getMatrixRow(int exDimension, int rowNum) {
        System.out.println("Row number: " + (rowNum + 1));
        if (!isFromFile) System.out.print(">");
        String[] factors = scanner.nextLine().trim().split(";");
        if (factors.length != exDimension) {
            System.out.println("Wrong dimension in factors (" + "row=" + (rowNum + 1) + "): you should input " + exDimension + " params.");
            if (isFromFile) System.exit(-1);
            return getMatrixRow(exDimension, rowNum);
        }
        double[] doubleFactors = new double[exDimension];
        for (int col = 0; col < exDimension; col++) {
            try {
                doubleFactors[col] = Double.valueOf(factors[col]);
            } catch (NumberFormatException ex) {
                System.out.println("Wrong value at position: [" + (rowNum + 1) + "" + (col + 1) + "]");
                if (isFromFile) System.exit(-1);
                return getMatrixRow(exDimension, rowNum);
            }
        }
        for (int col = 0; col < exDimension; col++) {
            if (col + 1 == exDimension) {
                System.out.print("b_" + (rowNum + 1) + "=" + doubleFactors[col] + "; ");
                break;
            }
            System.out.print("a_" + (col + 1) + "=" + doubleFactors[col] + "; ");
        }
        System.out.println();
        return doubleFactors;
    }

}
