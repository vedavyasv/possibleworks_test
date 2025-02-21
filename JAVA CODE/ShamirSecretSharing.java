import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShamirSecretSharing {
    static class Point {
        BigInteger x;
        BigInteger y;
        
        Point(BigInteger x, BigInteger y) {
            this.x = x;
            this.y = y;
        }
    }
    
    public static BigInteger lagrangeInterpolation(List<Point> points, int k) {
        BigInteger result = BigInteger.ZERO;
        
        // Evaluate at x = 0 to get the constant term
        for (int i = 0; i < k; i++) {
            BigInteger term = points.get(i).y;
            for (int j = 0; j < k; j++) {
                if (i != j) {
                    BigInteger numerator = points.get(j).x.negate();
                    BigInteger denominator = points.get(i).x.subtract(points.get(j).x);
                    term = term.multiply(numerator).divide(denominator);
                }
            }
            result = result.add(term);
        }
        return result;
    }
    
    public static BigInteger processTestCase(String filename) {
        try {
            // Read the entire file
            Scanner scanner = new Scanner(new File(filename));
            StringBuilder jsonContent = new StringBuilder();
            while (scanner.hasNextLine()) {
                jsonContent.append(scanner.nextLine().trim());
            }
            scanner.close();
            String jsonString = jsonContent.toString();
            
            // Extract n and k
            Pattern keysPattern = Pattern.compile("\"keys\"\\s*:\\s*\\{[^}]*\"n\"\\s*:\\s*(\\d+)[^}]*\"k\"\\s*:\\s*(\\d+)[^}]*\\}");
            Matcher keysMatcher = keysPattern.matcher(jsonString);
            if (!keysMatcher.find()) {
                throw new IllegalArgumentException("Invalid JSON format: keys not found");
            }
            int n = Integer.parseInt(keysMatcher.group(1));
            int k = Integer.parseInt(keysMatcher.group(2));
            
            // Parse points
            List<Point> points = new ArrayList<>();
            Pattern pointPattern = Pattern.compile("\"(\\d+)\"\\s*:\\s*\\{\\s*\"base\"\\s*:\\s*\"(\\d+)\"\\s*,\\s*\"value\"\\s*:\\s*\"([^\"]+)\"\\s*\\}");
            Matcher pointMatcher = pointPattern.matcher(jsonString);
            
            while (pointMatcher.find()) {
                int x = Integer.parseInt(pointMatcher.group(1));
                int base = Integer.parseInt(pointMatcher.group(2));
                String value = pointMatcher.group(3);
                
                // Convert value from given base to decimal
                BigInteger y = new BigInteger(value, base);
                points.add(new Point(BigInteger.valueOf(x), y));
            }
            
            if (points.size() < k) {
                throw new IllegalArgumentException("Not enough points found in JSON");
            }
            
            // Calculate secret using first k points
            return lagrangeInterpolation(points.subList(0, k), k);
            
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filename);
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static void main(String[] args) {
        String[] testFiles = {"testcase1.json", "testcase2.json"};
        
        for (String filename : testFiles) {
            BigInteger secret = processTestCase(filename);
            if (secret != null) {
                System.out.println("Secret for " + filename + ": " + secret);
            } else {
                System.out.println("Error processing " + filename);
            }
        }
    }
}