import java.rmi.Naming;
import java.util.List;
import java.util.Scanner;

public class ConferenceClient {
    private ConferenceService conferenceService;

    public ConferenceClient() {
        try {
            conferenceService = (ConferenceService) Naming.lookup("//localhost/ConferenceService");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void displayMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("1. Register participant");
            System.out.println("2. View participants");
            System.out.println("3. Export participants to XML");
            System.out.println("4. Import participants from XML");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    registerParticipant(scanner);
                    break;
                case 2:
                    viewParticipants();
                    break;
                case 3:
                    exportParticipantsToXML(scanner);
                    break;
                case 4:
                    importParticipantsFromXML(scanner);
                    break;
                case 5:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void registerParticipant(Scanner scanner) {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        Participant participant = new Participant();
        participant.setName(name);
        participant.setEmail(email);

        try {
            conferenceService.registerParticipant(participant);
            System.out.println("Participant registered successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void viewParticipants() {
        try {
            List<Participant> participants = conferenceService.getParticipants();
            if (participants.isEmpty()) {
                System.out.println("No participants registered.");
            } else {
                for (Participant participant : participants) {
                    System.out.println("Name: " + participant.getName() + ", Email: " + participant.getEmail());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void exportParticipantsToXML(Scanner scanner) {
        System.out.print("Enter file name to export to: ");
        String fileName = scanner.nextLine();

        try {
            conferenceService.exportParticipantsToXML(fileName);
            System.out.println("Participants exported to " + fileName + " successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void importParticipantsFromXML(Scanner scanner) {
        System.out.print("Enter file name to import from: ");
        String fileName = scanner.nextLine();

        try {
            conferenceService.importParticipantsFromXML(fileName);
            System.out.println("Participants imported from " + fileName + " successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ConferenceClient client = new ConferenceClient();
        client.displayMenu();
    }
}
