import java.lang.reflect.Array;

class ArrayUtils {

    public static Object createArray(Class<?> componentType, int length) {
        return Array.newInstance(componentType, length);
    }

    public static Object resizeArray(Object array, int newSize) {
        int oldSize = Array.getLength(array);
        Class<?> elementType = array.getClass().getComponentType();
        Object newArray = Array.newInstance(elementType, newSize);
        System.arraycopy(array, 0, newArray, 0, Math.min(oldSize, newSize));
        return newArray;
    }

    public static Object createMatrix(Class<?> componentType, int rows, int cols) {
        return Array.newInstance(componentType, rows, cols);
    }

    public static Object resizeMatrix(Object matrix, int newRows, int newCols) {
        int oldRows = Array.getLength(matrix);
        if (oldRows == 0) {
            return createMatrix(matrix.getClass().getComponentType(), newRows, newCols);
        }
        int oldCols = Array.getLength(Array.get(matrix, 0));
        Object newMatrix = Array.newInstance(matrix.getClass().getComponentType().getComponentType(), newRows, newCols);
        for (int i = 0; i < Math.min(oldRows, newRows); i++) {
            Object oldRow = Array.get(matrix, i);
            Object newRow = Array.get(newMatrix, i);
            System.arraycopy(oldRow, 0, newRow, 0, Math.min(oldCols, newCols));
        }
        return newMatrix;
    }

    // Перетворення масиву в рядок
    public static String arrayToString(Object array) {
        if (!array.getClass().isArray()) {
            return "Not an array";
        }

        StringBuilder sb = new StringBuilder();
        int length = Array.getLength(array);
        Class<?> componentType = array.getClass().getComponentType();


        if (componentType.isArray()) {
            sb.append(componentType.getComponentType().getTypeName() + "[][" + componentType.getName().split("\\[").length + "] = {");
            for (int i = 0; i < length; i++) {
                sb.append(arrayToString(Array.get(array, i)) + (i < length - 1 ? ", " : ""));
            }
        } else {
            sb.append(componentType.getTypeName() + "[" + length + "] = {");
            for (int i = 0; i < length; i++) {
                sb.append(Array.get(array, i) + (i < length - 1 ? ", " : ""));
            }
        }
        sb.append("}");
        return sb.toString();
    }
}
public class Zavd4 {

    public static void main(String[] args) {
        Object intArray = ArrayUtils.createArray(int.class, 2);
        System.out.println(ArrayUtils.arrayToString(intArray));

        Object stringArray = ArrayUtils.createArray(String.class, 3);
        System.out.println(ArrayUtils.arrayToString(stringArray));

        Object doubleArray = ArrayUtils.createArray(Double.class, 5);
        System.out.println(ArrayUtils.arrayToString(doubleArray));

        Object intMatrix = ArrayUtils.createMatrix(int.class, 3, 5);
        System.out.println(ArrayUtils.arrayToString(intMatrix));

        intMatrix = ArrayUtils.resizeMatrix(intMatrix, 4, 6);
        System.out.println(ArrayUtils.arrayToString(intMatrix));

        intMatrix = ArrayUtils.resizeMatrix(intMatrix, 3, 7);
        System.out.println(ArrayUtils.arrayToString(intMatrix));

        intMatrix = ArrayUtils.resizeMatrix(intMatrix, 2, 2);
        System.out.println(ArrayUtils.arrayToString(intMatrix));
    }
}