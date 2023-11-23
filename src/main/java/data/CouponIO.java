package data;

import java.io.*;
import java.util.StringTokenizer;

public class CouponIO {
    public static double getCouponDiscount(String couponCode, String filepath) {
        if (couponCode == null || couponCode.trim().isEmpty()) {
            return 0.0;
        }

        try (BufferedReader in = new BufferedReader(new FileReader(filepath))) {
            String line;
            while ((line = in.readLine()) != null) {
                StringTokenizer t = new StringTokenizer(line, "|");
                String code = t.nextToken();
                if (couponCode.equalsIgnoreCase(code)) {
                    double discount = Double.parseDouble(t.nextToken());
                    return discount;
                }
            }
        } catch (IOException e) {
            System.err.println(e);
        }
        return 0.0;
    }
}
