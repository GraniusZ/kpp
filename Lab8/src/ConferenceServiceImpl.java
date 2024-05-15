import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class ConferenceServiceImpl extends UnicastRemoteObject implements ConferenceService {
    private List<Participant> participants;

    protected ConferenceServiceImpl() throws RemoteException {
        participants = new ArrayList<>();
    }

    @Override
    public void registerParticipant(Participant participant) throws RemoteException {
        participants.add(participant);
    }

    @Override
    public List<Participant> getParticipants() throws RemoteException {
        return participants;
    }

    @Override
    public void exportParticipantsToXML(String fileName) throws RemoteException {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();
            Element rootElement = doc.createElement("participants");
            doc.appendChild(rootElement);

            for (Participant participant : participants) {
                Element participantElement = doc.createElement("participant");

                Element name = doc.createElement("name");
                name.appendChild(doc.createTextNode(participant.getName()));
                participantElement.appendChild(name);

                Element email = doc.createElement("email");
                email.appendChild(doc.createTextNode(participant.getEmail()));
                participantElement.appendChild(email);


                rootElement.appendChild(participantElement);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(fileName));
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void importParticipantsFromXML(String fileName) throws RemoteException {
        try {
            File inputFile = new File(fileName);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            participants.clear();
            NodeList nList = doc.getElementsByTagName("participant");
            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    String name = eElement.getElementsByTagName("name").item(0).getTextContent();
                    String email = eElement.getElementsByTagName("email").item(0).getTextContent();
                    Participant participant = new Participant();
                    participant.setName(name);
                    participant.setEmail(email);
                    participants.add(participant);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
