import java.util.Arrays;
import java.lang.reflect.Array;

public class Zavd4 {
    public static void main(String[] args) {
        int[] intArray = createIntArray(2);
        System.out.println("intArray: " + arrayToString(intArray));

        double[][] doubleArray = createDoubleMatrix(3, 3);
        System.out.println("doubleMatrix: \n" + matrixToString(doubleArray));

        Integer[] intRArray = createArray(Integer.class, 5, 0);
        System.out.println("IntengerArray: " + Arrays.toString(intRArray));

        Double[][] doubleRMatrix = createMatrix(Double.class, 2, 3, 0.0);
        System.out.println("DoubleMatrix: \n" + matrixToString(doubleRMatrix));

        intArray = resizeArray(intArray, 5);
        System.out.println("intArray resized: " + arrayToString(intArray));

        doubleArray = resizeMatrix(doubleArray, 4, 4);
        System.out.println("doubleArray resized: \n" + matrixToString(doubleArray));

        intRArray = resizeArray(intRArray, 10, Integer.class);
        System.out.println("IntengerArray resized: " + Arrays.toString(intRArray));

        doubleRMatrix = resizeMatrix(doubleRMatrix, 3, 5, Double.class);
        System.out.println("DoubleMatrix resized: \n" + matrixToString(doubleRMatrix));
    }
    public static int[] createIntArray(int size) {
        return new int[size];
    }
    public static double[][] createDoubleMatrix(int rows, int cols) {
        return new double[rows][cols];
    }
    public static <T> T[] createArray(Class<T> clazz, int size, T defaultValue) {
        @SuppressWarnings("unchecked")
        T[] array = (T[]) Array.newInstance(clazz, size);
        Arrays.fill(array, defaultValue);
        return array;
    }
    public static <T> T[][] createMatrix(Class<T> componentType, int rows, int cols, T defaultValue) {
        @SuppressWarnings("unchecked")
        T[][] matrix = (T[][]) Array.newInstance(componentType, rows, cols);
        for (int i = 0; i < matrix.length; i++) {
            matrix[i] = createArray(componentType, cols, defaultValue);
        }
        return matrix;
    }
    public static int[] resizeArray(int[] array, int newSize) {
        return Arrays.copyOf(array, newSize);
    }
    public static double[][] resizeMatrix(double[][] matrix, int newRows, int newCols) {
        double[][] newMatrix = Arrays.copyOf(matrix, newRows);
        for (int i = 0; i < newRows; i++) {
            if (i < matrix.length) {
                newMatrix[i] = Arrays.copyOf(matrix[i], newCols);
            } else {
                newMatrix[i] = new double[newCols];
            }
        }
        return newMatrix;
    }
    public static <T> T[] resizeArray(T[] array, int newSize, Class<T> clazz) {
        @SuppressWarnings("unchecked")
        T[] newArray = (T[]) Array.newInstance(clazz, newSize);
        System.arraycopy(array, 0, newArray, 0, Math.min(array.length, newSize));
        return newArray;
    }
    public static <T> T[][] resizeMatrix(T[][] matrix, int newRows, int newCols, Class<T> clazz) {
        @SuppressWarnings("unchecked")
        T[][] newMatrix = (T[][]) Array.newInstance(matrix[0].getClass(), newRows);
        for (int i = 0; i < newRows; i++) {
            if (i < matrix.length) {
                newMatrix[i] = resizeArray(matrix[i], newCols, clazz);
            } else {
                newMatrix[i] = createArray(clazz, newCols, null);
            }
        }
        return newMatrix;
    }
    public static String arrayToString(int[] array) {
        return Arrays.toString(array);
    }
    public static String matrixToString(double[][] matrix) {
        StringBuilder sb = new StringBuilder();
        for (double[] row : matrix) {
            sb.append(Arrays.toString(row)).append("\n");
        }
        return sb.toString();
    }
    public static String matrixToString(Double[][] matrix) {
        StringBuilder sb = new StringBuilder();
        for (Double[] row : matrix) {
            sb.append(Arrays.toString(row)).append("\n");
        }
        return sb.toString();
    }
}