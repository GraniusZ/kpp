import java.rmi.Naming;
import java.math.BigDecimal;

public class ComputeClient {

    public static void main(String[] args) {
        try {
            String name = "rmi://localhost/Compute";
            Compute comp = (Compute) Naming.lookup(name);
            int digits = 20;

            BigDecimal pi = comp.computePi(digits);
            System.out.println("Computed Pi: " + pi);

            BigDecimal e = comp.computeE(digits);
            System.out.println("Computed e: " + e);

        } catch (Exception e) {
            System.err.println("ComputeClient exception:");
            e.printStackTrace();
        }
    }
}
