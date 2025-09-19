// src/main/java/Calculator.java  (on main)
public class Calculator {
    public double add(double a, double b) { return a + b; }
    public double subtract(double a, double b) { return a - b; }
    public double multiply(double a, double b) { return a * b; }
    public double divide(double a, double b) {
        // main branch version: return infinity on division by zero
        if (b == 0) return Double.POSITIVE_INFINITY;
        return a / b;
    }
}
