package com.loanpro;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class LoanProCalculatorTest {

    private static String executeCommand(String operation, String num1, String num2) {
        String result = "";
        try {
            ProcessBuilder builder = new ProcessBuilder(
                    "docker", "run", "--rm", "public.ecr.aws/l4q9w4c5/loanpro-calculator-cli", operation, num1, num2);
            Process process = builder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                result += line;
            }
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) {
        LoanProCalculatorTest tester = new LoanProCalculatorTest();
        tester.runTests();
    }

    public void runTests() {
        testAddition();
        testSubtraction();
        testMultiplication();
        testDivision();
        testInvalidOperation();
        testLargeNumbers();
        testDecimalPrecision();
        testNegativeNumbers();
        testScientificNotation();
        testZeroDivision();
    }

    // Test for basic addition operation
    public void testAddition() {
        System.out.println("Test Addition:");
        System.out.println("Expected: 13.0");
        assert executeCommand("add", "8", "5").equals("13.0");
        System.out.println("Expected: -2.0");
        assert executeCommand("add", "-5", "3").equals("-2.0");
        System.out.println("Expected: 0.0");
        assert executeCommand("add", "0", "0").equals("0.0");
    }

    // Test for basic subtraction operation
    public void testSubtraction() {
        System.out.println("Test Subtraction:");
        System.out.println("Expected: 3.0");
        assert executeCommand("subtract", "8", "5").equals("3.0");
        System.out.println("Expected: -8.0");
        assert executeCommand("subtract", "-5", "3").equals("-8.0");
        System.out.println("Expected: 0.0");
        assert executeCommand("subtract", "0", "0").equals("0.0");
    }

    // Test for basic multiplication operation
    public void testMultiplication() {
        System.out.println("Test Multiplication:");
        System.out.println("Expected: 40.0");
        assert executeCommand("multiply", "8", "5").equals("40.0");
        System.out.println("Expected: -15.0");
        assert executeCommand("multiply", "-5", "3").equals("-15.0");
        System.out.println("Expected: 0.0");
        assert executeCommand("multiply", "0", "100").equals("0.0");
    }

    // Test for basic division operation
    public void testDivision() {
        System.out.println("Test Division:");
        System.out.println("Expected: 2.0");
        assert executeCommand("divide", "10", "5").equals("2.0");
        System.out.println("Expected: -5.0");
        assert executeCommand("divide", "-15", "3").equals("-5.0");
        System.out.println("Expected: 0.0");
        assert executeCommand("divide", "0", "10").equals("0.0");
    }

    // Test for invalid operation
    public void testInvalidOperation() {
        System.out.println("Test Invalid Operation:");
        System.out.println("Expected: Error message");
        String result = executeCommand("mod", "10", "5");
        assert result.contains("error") || result.contains("not recognized");
    }

    // Test for handling large numbers
    public void testLargeNumbers() {
        System.out.println("Test Large Numbers:");
        System.out.println("Expected: 1.0E16");
        assert executeCommand("add", "9999999999999999", "1").equals("1.0E16");
        System.out.println("Expected: 1.0");
        assert executeCommand("subtract", "10000000000000000", "9999999999999999").equals("1.0");
        System.out.println("Expected: 1.0E16");
        assert executeCommand("multiply", "100000000", "100000000").equals("1.0E16");
        System.out.println("Expected: 1.0E15");
        assert executeCommand("divide", "10000000000000000", "10").equals("1.0E15");
    }

    // Test for decimal precision handling
    public void testDecimalPrecision() {
        System.out.println("Test Decimal Precision:");
        System.out.println("Expected: 3.0E-8");
        assert executeCommand("add", "0.00000001", "0.00000002").equals("3.0E-8");
        System.out.println("Expected: 1.0");
        assert executeCommand("subtract", "1.00000001", "0.00000001").equals("1.0");
        System.out.println("Expected: 1.0E-10");
        assert executeCommand("multiply", "0.00001", "0.00001").equals("1.0E-10");
        System.out.println("Expected: 0.33333333");
        assert executeCommand("divide", "1", "3").equals("0.33333333");
    }

    // Test for handling negative numbers
    public void testNegativeNumbers() {
        System.out.println("Test Negative Numbers:");
        System.out.println("Expected: -10.0");
        assert executeCommand("add", "-5", "-5").equals("-10.0");
        System.out.println("Expected: 5.0");
        assert executeCommand("subtract", "-5", "-10").equals("5.0");
        System.out.println("Expected: -10.0");
        assert executeCommand("multiply", "-5", "2").equals("-10.0");
        System.out.println("Expected: 5.0");
        assert executeCommand("divide", "-10", "-2").equals("5.0");
    }

    // Test for handling scientific notation results
    public void testScientificNotation() {
        System.out.println("Test Scientific Notation:");
        System.out.println("Expected: 2.0E10");
        assert executeCommand("add", "1E10", "1E10").equals("2.0E10");
        System.out.println("Expected: 9.9999E9");
        assert executeCommand("subtract", "1E10", "1E5").equals("9.9999E9");
        System.out.println("Expected: 1.0E20");
        assert executeCommand("multiply", "1E10", "1E10").equals("1.0E20");
        System.out.println("Expected: 1.0E5");
        assert executeCommand("divide", "1E10", "1E5").equals("1.0E5");
    }

    // Test for division by zero
    public void testZeroDivision() {
        System.out.println("Test Zero Division:");
        System.out.println("Expected: Error message");
        assert executeCommand("divide", "10", "0").contains("error");
        System.out.println("Expected: 0.0");
        assert executeCommand("divide", "0", "10").equals("0.0");
        System.out.println("Expected: Error message");
        assert executeCommand("divide", "-10", "0").contains("error");
    }
}
