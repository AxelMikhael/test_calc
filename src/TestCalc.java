import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestCalc {

    public static void main (String args[]) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        System.out.println(Main.calc(input));
    }
}

class Main {
    public static String calc(String input) {

        // При помощи регулярного выражения отсеиваем все строки, не соответствующие заданному формату
        String regEx = "([1-9]|10)\\s*([+-/*])\\s*([1-9]|10)";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(input);
        if (m.matches())
            return calcHelperArabic(input);// Вызываем вспомогательый метод, вычисляющий результат операции

        else {
            String regEx2 = "(I|II|III|IV|V|VI|VII|VIII|IX|X)\\s*([+-/*])\\s*(I|II|III|IV|V|VI|VII|VIII|IX|X)";
            p = Pattern.compile(regEx2);
            m = p.matcher(input);
            if (m.matches())
                return calcHelperRome(input);
            else return "Исключение";
        }
    }

    private static String[] highlightOperand (String input) {

        // убираем все пробелы из строки
        input = input.replaceAll(" ", "");

        // выделяем из строки операнды
        String[] operandValue = input.split("[+,\\-,*,/]");
        return operandValue;
    }

    private static char highlightSign (String input) {

        // Выделяем из строки знак арифметической операции
        char sign ='0';
        for (int j=0; j<=input.length()-1; j++)
        {
            if ((input.charAt(j) == '+') | (input.charAt(j) == '*') |
                    (input.charAt(j) == '-')|(input.charAt(j) == '/')) {
                sign = input.charAt(j);
                break;
            }
        }
        return sign;
    }
    private static int calculatingValue (int[] operands, char signOperation) {
        // Вычисляем результат операции взависимости от знака операции
        int result = 0;

        switch (signOperation) {
            case '+':
                result = operands[0] + operands[1];
                break;
            case '-':
                result = operands[0] - operands[1];
                break;
            case '*':
                result = operands[0] * operands[1];
                break;
            case '/':
                result = operands[0] / operands[1];
                break;
        }
        return result;
    }


    private static String calcHelperArabic(String input) {

        String[] operandValue = highlightOperand(input);

        char operationSign = highlightSign(input);

        // преобразуем операнды из строчного формата в целочисленный
        int[] intOperand = {Integer.parseInt(operandValue[0]), Integer.parseInt(operandValue[1])};

        // Преобразуем результат операции в формат строки и возвращаем результат выполнения метода
        return Integer.toString(calculatingValue(intOperand, operationSign));
    }

    private static String calcHelperRome(String input) {

        int numbers[]  = {1, 4, 5, 9, 10, 50, 100, 500, 1000 };
        String letters[]  = { "I", "IV", "V", "IX", "X", "L", "C", "D", "M"};

        String[] operandValue = highlightOperand(input);

        char operationSign = highlightSign(input);

        // Преобразуем римские цифры в арабские
        int[] intOperand = new int[2];
        for (int n=0; n<=1; n++) {
            int sum=0;
            char chs[] = operandValue[n].toCharArray();
            for (int i = 0; i < chs.length; i++) {
                if (chs[i] == 'I') sum += 1;
                if (chs[i] == 'V') {
                    if (i != 0 && chs[i - 1] == 'I') sum += 4 - 1; // а также вычесть последнее накопленное сложение
                    else sum += 5;
                }
                if (chs[i] == 'X') {
                    if (i != 0 && chs[i - 1] == 'I') sum += 9 - 1;
                    else sum += 10;
                }
                if (chs[i] == 'L') {
                    if (i != 0 && chs[i - 1] == 'X') sum += 40 - 10;
                    else sum += 50;
                }
                if (chs[i] == 'C') {
                    if (i != 0 && chs[i - 1] == 'X') sum += 90 - 10;
                    else sum += 100;
                }
                if (chs[i] == 'D') {
                    if (i != 0 && chs[i - 1] == 'C') sum += 400 - 100;
                    else sum += 500;
                }
                if (chs[i] == 'M') {
                    if (i != 0 && chs[i - 1] == 'C') sum += 900 - 100;
                    else sum += 1000;
                }
            }
            intOperand[n] = sum;
        }

        int result = calculatingValue (intOperand, operationSign);

        // Проверяем условие, чтобы не получалсь отрицательного значения
        if (result <=0) return "Исключение";
            // Преобразуем арабские цифры в римские
        else {
            String romanValue = "";
            int N = result;
            while ( N > 0 ){
                for (int i = 0; i < numbers.length; i++){
                    if ( N < numbers[i] ){
                        N -= numbers[i-1];
                        romanValue += letters[i-1];
                        break;
                    }
                }
            }

            return romanValue;
        }
    }
}