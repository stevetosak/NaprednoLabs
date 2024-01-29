package Labs1.Vezba2;

import java.util.*;
import java.util.stream.IntStream;


class RomanConverter {
    /**
     * Roman to decimal converter
     *
     * @param n in decimal format
     * @return string representation of the number in Roman numeral
     */

    private static final int[] values = {1000,900,500,400,100,90,50,40,10,9,5,4,1};
    private static final String [] keys = {"M","CM","D","CD","C","XC","L","XL","X","IX","V","IV","I"};

    //ova ti sa vikat mapa od ali express
    public static String toRoman(int n) {
        StringBuilder buffer = new StringBuilder();

        for (int i = 0; i < values.length; i++) {
            if(n == values[i]){
                return keys[i];
            }
        }
            for (int j = 0; n != 0 ; j++) {
                if(n >= values[j]){
                    buffer.append(keys[j]);
                    n -= values[j];
                    --j;
                }
            }

        return buffer.toString();
    }

}

public class RomanConverterTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        IntStream.range(0, n)
                .forEach(x -> System.out.println(RomanConverter.toRoman(scanner.nextInt())));
        scanner.close();
    }
}
