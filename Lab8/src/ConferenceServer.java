import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class ConferenceServer {
    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1099);
            ConferenceService conferenceService = new ConferenceServiceImpl();
            Naming.rebind("ConferenceService", conferenceService);
            System.out.println("ConferenceService is running...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
