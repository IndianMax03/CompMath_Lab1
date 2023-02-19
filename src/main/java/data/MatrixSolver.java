package data;

import java.util.Scanner;

public class MatrixSolver {

    private final MatrixData matrixData;

    public MatrixSolver(MatrixData matrixData) {
        this.matrixData = matrixData;
    }

    public void solve() {
        System.out.println("Checking diagonal dominance...");
        if (!isDiagonalDominance()) {
            System.out.println("Trying to fix diagonal dominance...\n");
            correctDiagonalDominance();
            System.out.println("New matrix is: ");
            System.out.println(matrixData);
            if (!isDiagonalDominance()) {
                System.out.println("The algorithm failed to reach fully diagonal dominance\n");
            } else {
                System.out.println("Fully diagonal dominance reached!\n");
            }
        } else {
            System.out.println("Matrix already diagonal dominance\n");
        }
        createNormedMatrix();
        System.out.println(matrixData.normedMatrixToString());
        try {
            iterate();
        } catch (OutOfMemoryError error) {
            System.out.println("Memory ran out. Try to fix accuracy value.");
        }
    }


    private boolean isDiagonalDominance() {
        boolean diagonalDominance = true;
        int dimension = matrixData.getDIMENSION();
        final double[][] matrix = matrixData.getMatrix();
        for (int row = 0; row < dimension; row++) {
            double diagonalElement = 0;
            double rowSum = 0;
            for (int column = 0; column < dimension; column++) {
                if (column != row) {
                    rowSum += Math.abs(matrix[row][column]);
                } else {
                    diagonalElement = Math.abs(matrix[row][column]);
                }
            }
            if (diagonalElement < rowSum) {
                diagonalDominance = false;
                break;
            }
        }
        return diagonalDominance;
    }

    private void correctDiagonalDominance() {
        final double[][] matrix = matrixData.getMatrix();
        int dimension = matrixData.getDIMENSION();
        for (int column = 0; column < dimension; column++) {
            double diagonalElement = matrix[column][column];
            int dominanceRowNumber = column;
            double[] dominanceRow = matrix[column];
            for (int row = column; row < dimension; row++) {
                if (Math.abs(matrix[row][column]) > Math.abs(diagonalElement)) {
                    dominanceRow = matrix[row];
                    dominanceRowNumber = row;
                    diagonalElement = Math.abs(matrix[row][column]);
                }
            }
            matrix[dominanceRowNumber] = matrix[column];
            matrix[column] = dominanceRow;
        }
        matrixData.setMatrix(matrix);
    }

    private void createNormedMatrix() {
        double[][] matrix = matrixData.getMatrix();
        int dimension = matrixData.getDIMENSION();
        int exDimension = matrixData.getEXTENDED_DIMENSION();
        double epsilon = matrixData.getEPSILON();
        double rounder = 100;
        if (epsilon < 1) {
            rounder = (10 / epsilon / epsilon / epsilon);
        }
        for (int row = 0; row < dimension; row++) {
            double diagonalElement = matrix[row][row];
            for (int column = 0; column < exDimension; column++) {
                matrix[row][column] = Math.round((matrix[row][column] / diagonalElement * (-1d)) * rounder) / rounder;
                if (row == column) {
                    matrix[row][column] = 0d;
                }
                if (column == exDimension - 1) {
                    matrix[row][column] *= (-1d);
                }
            }
        }
        matrixData.setNormedMatrix(matrix);
    }

    private void iterate() {
        int k = 0;
        double[][] normedMatrix = matrixData.getNormedMatrix();
        int dimension = matrixData.getDIMENSION();
        int exDimension = matrixData.getEXTENDED_DIMENSION();
        double epsilon = matrixData.getEPSILON();
        double rounder = 100;
        if (epsilon < 1) {
            rounder = (10 / epsilon / epsilon / epsilon);
        }
        int maxIterationsNumber = (int) (100 * epsilon);
        if (epsilon < 1) {
            maxIterationsNumber = (int) (100 / epsilon);
        }
        Double[][] iterations = new Double[maxIterationsNumber][exDimension];
        for (int row = 0; row < dimension; row++) {
            iterations[k][row] = (Math.round(normedMatrix[row][dimension] * rounder) / rounder);
        }
        iterations[k][exDimension - 1] = 0d;

        k += 1;
        for (int column = 0; column < dimension; column++) {
            iterations[k][column] = iterations[k - 1][column];
        }
        try {
            while (true) {
                for (int row = 0; row < dimension; row++) {
                    double sum = 0;
                    for (int column = 0; column < exDimension; column++) {
                        if (column == row) continue;
                        if (column == dimension) {
                            sum += Math.round(normedMatrix[row][column] * rounder) / rounder;
                            break;
                        }
                        sum += Math.round(normedMatrix[row][column] * iterations[k][column] * rounder) / rounder;
                    }
                    iterations[k][row] = (Math.round(sum * rounder) / rounder);
                }
                double maxAccuracy = Math.abs(iterations[k][0] - iterations[k - 1][0]);
                for (int column = 0; column < dimension; column++) {
                    if (Math.abs(iterations[k][column] - iterations[k - 1][column]) > maxAccuracy) {
                        maxAccuracy = Math.abs(Math.round((iterations[k][column] - iterations[k - 1][column]) * rounder) / rounder);
                    }
                }
                iterations[k][exDimension - 1] = (Math.round(maxAccuracy * rounder) / rounder);
                k += 1;
                if (maxAccuracy < epsilon) {
                    break;
                }
                for (int column = 0; column < dimension; column++) {
                    iterations[k][column] = iterations[k - 1][column];
                }
            }
        } catch (ArrayIndexOutOfBoundsException exception) {
            System.out.println("The algorithm cannot solve system by " + k + " iterations. Try other data.");
            System.out.print("Would you like to see first 20 iterations to correct your accuracy? (y/n)\n>");
            String ans = new Scanner(System.in).nextLine().trim();
            if (!ans.equals("y")) {
                return;
            } else {
                k = 20;
            }
        }

        System.out.println("There is " + k + " (max= " + maxIterationsNumber + ") iterations ");
        for (int row = 0; row < k; row++) {
            if (row == 0) {
                System.out.print("N| ");
                for (int col = 0; col < exDimension; col++) {
                    if (col == exDimension - 1) {
                        System.out.print("{epsilon};");
                        break;
                    }
                    System.out.print("{x_" + (col + 1) + "}; ");
                }
                System.out.println();
            }
            System.out.print(row + "| ");
            for (int var = 0; var < exDimension; var++) {
                System.out.printf("{" + iterations[row][var] + "}; ");
            }
            System.out.println();
        }
    }

}
