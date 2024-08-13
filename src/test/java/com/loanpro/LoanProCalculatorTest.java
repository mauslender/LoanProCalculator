package com.loanpro;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoanProCalculatorTest {

    private String executeCommand(String operation, String num1, String num2) {
        StringBuilder result = new StringBuilder();
        try {
            ProcessBuilder builder = new ProcessBuilder(
                    "docker", "run", "--rm", "public.ecr.aws/l4q9w4c5/loanpro-calculator-cli", operation, num1, num2);
            builder.redirectErrorStream(true);
            Process process = builder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    @Test
    public void testAddition() {
        System.out.println("Test Addition:");
        assertEquals("Result: 13", executeCommand("add", "8", "5"));
        assertEquals("Result: -2", executeCommand("add", "-5", "3"));
        assertEquals("Result: 0", executeCommand("add", "0", "0"));
    }

    @Test
    public void testSubtraction() {
        System.out.println("Test Subtraction:");
        assertEquals("Result: 3", executeCommand("subtract", "8", "5"));
        assertEquals("Result: -8", executeCommand("subtract", "-5", "3"));
        assertEquals("Result: 0", executeCommand("subtract", "0", "0"));
    }

    @Test
    public void testMultiplication() {
        System.out.println("Test Multiplication:");
        assertEquals("Result: 40", executeCommand("multiply", "8", "5"));
        assertEquals("Result: -15", executeCommand("multiply", "-5", "3"));
        assertEquals("Result: 0", executeCommand("multiply", "0", "100"));
    }

    @Test
    public void testDivision() {
        System.out.println("Test Division:");
        assertEquals("Result: 2", executeCommand("divide", "10", "5"));
        assertEquals("Result: -5", executeCommand("divide", "-15", "3"));
        assertEquals("Result: 0", executeCommand("divide", "0", "10"));
    }

    @Test
    public void testInvalidOperation() {
        System.out.println("Test Invalid Operation:");
        String result = executeCommand("mod", "10", "5");
        assertEquals("Error: Unknown operation: mod", result);
    }

    @Test
    public void testLargeNumbers() {
        System.out.println("Test Large Numbers:");
        assertEquals("Result: 10000000000000000", executeCommand("add", "9999999999999999", "1"));
        assertEquals("Result: 9999999999999004", executeCommand("subtract", "10000000000000000", "995"));
        assertEquals("Result: 10000000000000000", executeCommand("multiply", "100000000", "100000000"));
        assertEquals("Result: 1000000000000000", executeCommand("divide", "10000000000000000", "10"));
    }

    @Test
    public void testDecimalPrecision() {
        System.out.println("Test Decimal Precision:");
        assertEquals("Result: 0.00000003", executeCommand("add", "0.00000001", "0.00000002"));
        assertEquals("Result: 1", executeCommand("subtract", "1.00000001", "0.00000001"));
        assertEquals("Result: 0", executeCommand("multiply", "0.00001", "0.00001"));
        assertEquals("Result: 0.33333333", executeCommand("divide", "1", "3"));
    }

    @Test
    public void testNegativeNumbers() {
        System.out.println("Test Negative Numbers:");
        assertEquals("Result: -10", executeCommand("add", "-5", "-5"));
        assertEquals("Result: 5", executeCommand("subtract", "-5", "-10"));
        assertEquals("Result: -10", executeCommand("multiply", "-5", "2"));
        assertEquals("Result: 5", executeCommand("divide", "-10", "-2"));
    }

    @Test
    public void testScientificNotation() {
        System.out.println("Test Scientific Notation:");
        assertEquals("Result: 20000000000", executeCommand("add", "1E10", "1E10"));
        assertEquals("Result: 9999900000", executeCommand("subtract", "1E10", "1E5"));
        assertEquals("Result: 100000000000000000000", executeCommand("multiply", "1E10", "1E10"));
        assertEquals("Result: 100000", executeCommand("divide", "1E10", "1E5"));
    }

    @Test
    public void testZeroDivision() {
        System.out.println("Test Zero Division:");
        assertEquals("Error: Cannot divide by zero", executeCommand("divide", "10", "0"));
        assertEquals("Result: 0", executeCommand("divide", "0", "10"));
        assertEquals("Error: Cannot divide by zero", executeCommand("divide", "-10", "0"));
    }
}
