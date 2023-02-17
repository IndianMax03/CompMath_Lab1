package data;

import exceptions.WrongDataException;

import java.util.ArrayList;

public class MatrixData {
    private final int DIMENSION;
    private final int EXTENDED_DIMENSION;
    private final Double EPSILON;
    private final MatrixDataValidator VALIDATOR = MatrixDataValidator.getInstance();
    private double[][] matrix;

    private double[][] normedMatrix;

    public MatrixData(String dimension, String epsilon) throws WrongDataException {
        DIMENSION = VALIDATOR.validateDimension(dimension);
        EXTENDED_DIMENSION = DIMENSION + 1;
        EPSILON = VALIDATOR.validateEpsilon(epsilon);
    }

    public int getDIMENSION() {
        return DIMENSION;
    }

    public int getEXTENDED_DIMENSION() {
        return EXTENDED_DIMENSION;
    }

    public Double getEPSILON() {
        return EPSILON;
    }

    public double[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(double[][] matrix) {
        this.matrix = matrix;
    }

    public double[][] getNormedMatrix() {
        return normedMatrix;
    }

    public void setNormedMatrix(double[][] normedMatrix) {
        this.normedMatrix = normedMatrix;
    }

    public String normedMatrixToString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Normed matrix:\n");
        for (int row = 0; row < DIMENSION; row++) {
            builder.append("x_").append(row + 1).append(" = ");
            boolean firstAppending = true;
            for (int column = 0; column < EXTENDED_DIMENSION; column++) {
                if (row == column) continue;
                if (column == EXTENDED_DIMENSION - 1) {
                    if (normedMatrix[row][column] >= 0) {
                        builder.append(" + ").append(matrix[row][column]);
                    } else {
                        builder.append(normedMatrix[row][column]);
                    }
                    continue;
                }
                if (!firstAppending) {
                    if (normedMatrix[row][column] >= 0) {
                        builder.append(" + ").append(matrix[row][column]).append("*x_").append(column + 1);
                    } else {
                        builder.append(" ").append(normedMatrix[row][column]).append("*x_").append(column + 1);
                    }
                } else {
                    builder.append(normedMatrix[row][column]).append("*x_").append(column + 1);
                    firstAppending = false;
                }
            }
            builder.append(";\n");
        }
        return builder.toString();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Matrix (n=").append(DIMENSION).append(", epsilon=").append(EPSILON).append("):\n");
        for (int i = 0; i < DIMENSION; i++) {
            for (int j = 0; j < EXTENDED_DIMENSION; j++) {
                if (j + 1 == EXTENDED_DIMENSION) {
                    builder.append(" = ").append(matrix[i][j]).append(";\n");
                    break;
                }
                if (matrix[i][j] >= 0 && j != 0) {
                    builder.append(" + ").append(matrix[i][j]).append("*x_").append(j + 1);
                } else {
                    builder.append(matrix[i][j]).append("*x_").append(j + 1);
                }
            }
        }
        return builder.toString();
    }
}
