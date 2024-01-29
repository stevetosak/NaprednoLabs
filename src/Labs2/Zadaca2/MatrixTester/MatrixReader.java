package Labs2.Zadaca2.MatrixTester;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MatrixReader {
    public static DoubleMatrix read (InputStream input) throws IOException, InsufficientElementsException {
        InputStreamReader inpt = new InputStreamReader(input);
        BufferedReader reader = new BufferedReader(inpt);

        String[] line1 = reader.readLine().split(" ");
        int numRows = Integer.parseInt(line1[0]);
        int numColumns = Integer.parseInt(line1[1]);
        double [] arr = new double[numRows * numColumns];
        int k = 0;

        for(int i = 0; i < numRows; i++){
            String[] rows = reader.readLine().split(" ");
            for(int j = 0; j < numColumns; j++ ){
                arr[k++] = Double.parseDouble(rows[j]);
            }

        }
        return new DoubleMatrix(arr,numRows,numColumns);

    }
}
