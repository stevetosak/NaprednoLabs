package Labs2.Zadaca2.MatrixTester;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class DoubleMatrix {

    private final double[][] matrix;
    private final int rows;
    private final int columns;
    DoubleMatrix(double arr[], int m, int n) throws InsufficientElementsException {
        if(arr.length < m * n){
            throw new InsufficientElementsException("Insufficient number of elements");
        }
        double [] localArray = arr.clone();
        this.rows = m;
        this.columns = n;

        int start = arr.length - (m * n);
        matrix = new double[m][n];
        for(int i = 0; i < m; i++){
            for(int j = 0; j < n; j++){
                matrix[i][j] = localArray[start++];
            }
        }
    }
    public String getDimensions(){
        return "[" + rows + " x " + columns + "]";
    }

    public int rows(){
        return rows;
    }
    public int columns(){
        return columns;
    }
    double maxElementAtRow(int row) throws InvalidRowNumberException {
        if(row > rows || row < 1){
            throw new InvalidRowNumberException("Invalid row number");
        }
        double max = matrix[row-1][0];
        for(int i = 1; i < columns; i++){
            if(matrix[row-1][i] > max){
                max = matrix[row-1][i];
            }
        }
        return max;
    }
    double maxElementAtColumn(int column) throws InvalidColumnNumberException {
        if(column > columns || column < 1){
            throw new InvalidColumnNumberException("Invalid column number");
        }
        double max = matrix[0][column-1];
        for(int i = 1; i < rows; i++){
            if(matrix[i][column-1] > max){
                max = matrix[i][column-1];
            }
        }
        return max;
    }

    double sum(){
        double sum = 0;
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < columns; j++){
                sum += matrix[i][j];
            }
        }
        return sum;
    }

    double [] toSortedArray(){
        Double [] array = new Double[rows * columns];
        int k = 0;

        for(int i = 0; i < rows; i++){
            for(int j = 0; j < columns; j++){
                array[k++] = matrix[i][j];
            }
        }
        Arrays.sort(array,Collections.reverseOrder());
        return Stream.of(array).mapToDouble(Double::doubleValue).toArray();
    }
    public boolean isEqualMatrix(DoubleMatrix m1,DoubleMatrix m2){
        if(m1.rows != m2.rows && m1.columns != m2.columns){
            return false;
        }

        for(int i = 0; i < m1.rows; i++){
            for(int j = 0; j < m1.columns; j++){
                if(m1.matrix[i][j] != m2.matrix[i][j]){
                    return false;
                }
            }
        }
        return true;
    }
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        for(int i = 0; i < rows; i++){
            for(int j = 0; j < columns-1;j++){
               result.append(String.format("%.2f\t",matrix[i][j]));
            }
            if(i != rows-1){
                result.append(String.format("%.2f\n",matrix[i][columns-1]));
            }
            else{
                result.append(String.format("%.2f\n",matrix[i][columns-1]));
            }
        }
        return result.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DoubleMatrix that = (DoubleMatrix) o;
        return rows == that.rows && columns == that.columns && isEqualMatrix(this,that);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(rows, columns);
        result = 31 * result + Arrays.deepHashCode(matrix);
        return result;
    }
}
