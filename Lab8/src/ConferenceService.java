import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ConferenceService extends Remote {
    void registerParticipant(Participant participant) throws RemoteException;
    List<Participant> getParticipants() throws RemoteException;
    void exportParticipantsToXML(String fileName) throws RemoteException;
    void importParticipantsFromXML(String fileName) throws RemoteException;
}
