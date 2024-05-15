import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.math.BigDecimal;
import java.math.MathContext;

public class ComputeEngine extends UnicastRemoteObject implements Compute {

    protected ComputeEngine() throws RemoteException {
        super();
    }

    @Override
    public BigDecimal computePi(int digits) throws RemoteException {
        System.out.println("computePi called with digits: " + digits);
        MathContext mc = new MathContext(digits);
        return computePiWithFormula(mc);
    }

    @Override
    public BigDecimal computeE(int digits) throws RemoteException {
        System.out.println("computeE called with digits: " + digits);
        MathContext mc = new MathContext(digits);
        return computeEWithFormula(mc);
    }

    private BigDecimal computePiWithFormula(MathContext mc) {
        System.out.println("Starting computePiWithFormula");
        BigDecimal arctan1_5 = arctan(BigDecimal.valueOf(1.0 / 5.0), mc);
        BigDecimal arctan1_239 = arctan(BigDecimal.valueOf(1.0 / 239.0), mc);
        BigDecimal pi = arctan1_5.multiply(new BigDecimal(4)).subtract(arctan1_239).multiply(new BigDecimal(4));
        return pi.round(mc);
    }

    private BigDecimal arctan(BigDecimal x, MathContext mc) {
        System.out.println("Starting arctan with x = " + x);
        BigDecimal result = BigDecimal.ZERO;
        BigDecimal term;
        BigDecimal xSquared = x.multiply(x, mc);
        BigDecimal numerator = x;
        BigDecimal denominator = BigDecimal.ONE;
        boolean addTerm = true;
        int iteration = 0;
        do {
            term = numerator.divide(denominator, mc);
            result = addTerm ? result.add(term, mc) : result.subtract(term, mc);
            numerator = numerator.multiply(xSquared, mc);
            denominator = denominator.add(new BigDecimal(2), mc);
            addTerm = !addTerm;
            iteration++;
            if (iteration % 1000 == 0) {
                System.out.println("Iteration: " + iteration + ", term = " + term);
            }
        } while (term.compareTo(BigDecimal.ZERO) != 0 && iteration < mc.getPrecision());
        System.out.println("Ending arctan with result = " + result);
        return result;
    }

    private BigDecimal computeEWithFormula(MathContext mc) {
        System.out.println("Starting computeEWithFormula");
        BigDecimal e = BigDecimal.ONE;
        BigDecimal fact = BigDecimal.ONE;
        for (int i = 1; i < mc.getPrecision(); i++) {
            fact = fact.multiply(new BigDecimal(i), mc);
            e = e.add(BigDecimal.ONE.divide(fact, mc), mc);
        }
        System.out.println("Ending computeEWithFormula with result = " + e);
        return e;
    }

    public static void main(String[] args) {
        try {
            Compute engine = new ComputeEngine();
            LocateRegistry.createRegistry(1099);
            Naming.rebind("Compute", engine);
            System.out.println("ComputeEngine bound in registry");
        } catch (Exception e) {
            System.err.println("ComputeEngine exception:");
            e.printStackTrace();
        }
    }
}
