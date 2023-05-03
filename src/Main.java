//При выполнении задания старался расставить ошибки на разные случаи

import java.util.Scanner;
import java.util.TreeMap;

public class Main {
    //    создал булевые поля для проброса ошибки при введении разных систем счисления
    private static boolean isRomanNumber = false;
    private static boolean isArabicNumber = false;
    //    мапа для перевода из арабской в римскую систему счисления
    private static TreeMap<Integer, String> romanIntStr = new TreeMap<>();
    //    мапа для перевода из римской в арабскую систему счисления
    private static TreeMap<String, Integer> romanStrInt = new TreeMap<>();

    static {
        romanIntStr.put(100, "C");
        romanIntStr.put(90, "XC");
        romanIntStr.put(50, "L");
        romanIntStr.put(40, "XL");
        romanIntStr.put(10, "X");
        romanIntStr.put(9, "IX");
        romanIntStr.put(5, "V");
        romanIntStr.put(4, "IV");
        romanIntStr.put(1, "I");

        romanStrInt.put("I", 1);
        romanStrInt.put("V", 5);
        romanStrInt.put("X", 10);
    }

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        System.out.println(calc(scanner.nextLine()));
    }

    public static String calc(String input) throws Exception {
//        считал что введеные символы будут разделены пробелом
        if (input.length() < 5) throw new CalculatorException("строка не является математической операцией");

        String[] operationArray = {"+", "-", "*", "/"};
        String[] regex = {"\\+", "-", "\\*", "/"};

        int operation = -1;

        for (int i = 0; i < operationArray.length; i++) {
            if (input.contains(operationArray[i])) operation = i;
        }

        if (operation == -1) throw new CalculatorException("Неизвестная операция, допустимые: +, -, /, *");

        String[] values = input.split(regex[operation]);

//        Добавил проверку на ввод более двух операндов
        if (values.length > 2) throw new CalculatorException("формат математической операции не удовлетворяет заданию - два операнда и один оператор (+, -, /, *)");

        String value1 = values[0].trim();
        String value2 = values[1].trim();

        int num1 = toNumber(value1);
        int num2 = toNumber(value2);

        if (isRomanNumber && isArabicNumber) throw new CalculatorException("используются одновременно разные системы счисления");
        if (isRomanNumber && operation == 1) throw new CalculatorException("в римской системе нет отрицательных чисел");


        int result = switch (operation) {
            case 0 -> num1 + num2;
            case 1 -> num1 - num2;
            case 2 -> num1 * num2;
            case 3 -> num1 / num2;
            default -> 0;
        };

        String resultString;

        if (isRomanNumber) {
            if (result < 1) throw new CalculatorException("при работе с римскими числами результат не должен быть меньше 1");
            resultString = intToRoman(result);
        } else resultString = String.valueOf(result);

        return resultString;
    }

    //    перевод числа из арабской в римскую систему счисления
    private static String intToRoman(int num) {
        StringBuilder roman = new StringBuilder();

        while (num != 0) {
            int temp = romanIntStr.floorKey(num);
            roman.append(romanIntStr.get(temp));
            num -= temp;
        }
        return roman.toString();
    }

    //    получение числа из строки. в кэтч записал поиск римского числа для того что бы выделить ошибку с неверным символом (типо если вдруг опечатка)
    private static int toNumber(String value) throws CalculatorException {
        int num = 0;

        try {
            num = Integer.parseInt(value);
            isArabicNumber = true;
        } catch (NumberFormatException e) {
            String[] romanNum = value.split("");
            int len = romanNum.length;

            for (int i = len - 1; i >= 0; i--) {
                if (!romanStrInt.containsKey(romanNum[i])) throw new CalculatorException("неверный символ");
            }

            num += romanStrInt.get(romanNum[len - 1]);
            for (int i = len - 2; i >= 0 ; i--) {
                int temp = romanStrInt.get(romanNum[i]);
                if (temp < romanStrInt.get(romanNum[i + 1])) num -= temp;
                else num += temp;
            }
            isRomanNumber = true;
        }

        if (num < 1 || num > 10) throw new CalculatorException("допустимо использовать числа от 1 до 10");
        return num;
    }
}

class CalculatorException extends Exception {
    CalculatorException(String message) {
        super(message);
    }
}

// вообщем сделал бы чуть по другому если бы на ошибки не акцентировал внимание