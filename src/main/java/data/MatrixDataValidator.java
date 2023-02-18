package data;

import exceptions.WrongDataException;

public class MatrixDataValidator {

    private static MatrixDataValidator instance;
    private static final int MAX_DIMENSION = 21;
    private static final int MIN_DIMENSION = 0;
    private static final int MIN_EPSILON = 0;

    private MatrixDataValidator() {
    }

    public static MatrixDataValidator getInstance() {
        if (instance == null) {
            instance = new MatrixDataValidator();
        }
        return instance;
    }

    public Integer validateDimension(String dimension) throws WrongDataException {
        int dimensionValue;
        try {

            dimensionValue = Integer.parseInt(dimension);
            if (!(dimensionValue > MIN_DIMENSION && dimensionValue < MAX_DIMENSION)) {
                throw new WrongDataException("Dimension should be between: " + MIN_DIMENSION + " and " + MAX_DIMENSION);
            }
            return dimensionValue;

        } catch (NumberFormatException exception) {
            throw new WrongDataException("Wrong dimension data type (expected integer)");
        }
    }

    public double validateEpsilon(String epsilon) throws WrongDataException {
        double epsilonValue;
        try {
            epsilonValue = Double.parseDouble(epsilon.replaceAll(",", "."));

            if (!(epsilonValue > MIN_EPSILON)) {
                throw new WrongDataException("Epsilon should be higher than: " + MIN_EPSILON);
            }
            return epsilonValue;

        } catch (NumberFormatException exception) {
            throw new WrongDataException("Wrong epsilon data type (expected double)");
        }
    }

}
