import java.rmi.Remote;
import java.rmi.RemoteException;
import java.math.BigDecimal;

public interface Compute extends Remote {
    BigDecimal computePi(int digits) throws RemoteException;
    BigDecimal computeE(int digits) throws RemoteException;
}
