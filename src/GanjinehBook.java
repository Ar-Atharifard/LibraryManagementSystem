import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

class GanjinehBook extends Resource {
    private final String authorName;
    private final String publisherName;
    private final String printYear;
    private final String donorName;
    private final ArrayList<LocalDateTime[]> readSheet;

    public GanjinehBook(String id, String name, String authorName, String publisherName, String printYear, String donorName, Category category, Library library) {
        super(id, library, name, category);
        this.authorName = authorName;
        this.publisherName = publisherName;
        this.printYear = printYear;
        this.donorName = donorName;
        library.getResources().put(this.getId(), this);
        readSheet = new ArrayList<>();
    }

    public ArrayList<LocalDateTime[]> getReadSheet() {
        return readSheet;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public static void addGanjinehBook(String[] cmdArr) {
        if (checkErrorsForAddResource(cmdArr[1], cmdArr[2], cmdArr[3], cmdArr[9], cmdArr[10])) {
            new GanjinehBook(cmdArr[3], cmdArr[4], cmdArr[5], cmdArr[6], cmdArr[7], cmdArr[8], LibraryIMS.categories.get(cmdArr[9]), LibraryIMS.libraries.get(cmdArr[10]));
            System.out.println("success");
        }
    }

    private void read(LocalDateTime start, LocalDateTime end) {
        readSheet.add(new LocalDateTime[]{start, end});
    }

    public static void readCmd(String[] cmdArr) {
        if (LibraryIMS.people.containsKey(cmdArr[1]) && LibraryIMS.libraries.containsKey(cmdArr[3]) && LibraryIMS.libraries.get(cmdArr[3]).getResources().containsKey(cmdArr[4])) {
            Library library = LibraryIMS.libraries.get(cmdArr[3]);
            Person person = LibraryIMS.people.get(cmdArr[1]);
            Resource resource = library.getResources().get(cmdArr[4]);
            LocalDateTime readStartTime = LocalDateTime.from(DateTimeFormatter.ofPattern("y-M-d-H:m").parse(cmdArr[5] + "-" + cmdArr[6]));
            if (person instanceof Professor) {
                if (person.getPassword().equals(cmdArr[2])) {
                    if (resource instanceof GanjinehBook && ((GanjinehBook) resource).ganjinehBookIsAvailable(readStartTime, readStartTime.plusHours(2)) && ((Professor) person).getDebt() == 0) {
                        ((GanjinehBook) resource).read(readStartTime, readStartTime.plusHours(2));
                        System.out.println("success");
                    } else {
                        System.out.println("not-allowed");
                    }
                } else {
                    System.out.println("invalid-pass");
                }
            } else {
                System.out.println("permission-denied");
            }
        } else {
            System.out.println("not-found");
        }
    }

    private boolean ganjinehBookIsAvailable(LocalDateTime start, LocalDateTime end) {
        for (LocalDateTime[] busyTime : readSheet) {
            if ((busyTime[0].isAfter(start) || busyTime[0].isEqual(start)) && busyTime[0].isBefore(end) || (start.isAfter(busyTime[0]) || start.isEqual(busyTime[0])) && start.isBefore(busyTime[1])) {
                return false;
            }
        }
        return true;
    }

}