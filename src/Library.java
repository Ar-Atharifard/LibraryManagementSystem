import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

class Library {
    private final String id;
    private final String name;
    private final int yearOfFoundation;
    private final int numOfDecks;
    private final String address;
    private final HashMap<String, Manager> managers;
    private final HashMap<String, Resource> resources;
    private final HashMap<String, Borrowing> borrowings;

    public Library(String id, String name, int yearOfFoundation, int numOfDecks, String address) {
        this.id = id;
        this.name = name;
        this.yearOfFoundation = yearOfFoundation;
        this.numOfDecks = numOfDecks;
        this.address = address;
        managers = new HashMap<>();
        resources = new HashMap<>();
        LibraryIMS.libraries.put(this.id, this);
        borrowings = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public HashMap<String, Manager> getManagers() {
        return managers;
    }

    public HashMap<String, Resource> getResources() {
        return resources;
    }

    public HashMap<String, Borrowing> getBorrowings() {
        return borrowings;
    }

    public static void addLibrary(String[] cmdArr) {
        if (!LibraryIMS.people.containsKey(cmdArr[1])) {
            System.out.println("not-found");
        } else {
            if (LibraryIMS.people.get(cmdArr[1]).getPassword().equals(cmdArr[2])) {
                if (LibraryIMS.people.get(cmdArr[1]) instanceof Admin) {
                    if (LibraryIMS.libraries.containsKey(cmdArr[3])) {
                        System.out.println("duplicate-id");
                    } else {
                        new Library(cmdArr[3], cmdArr[4], Integer.parseInt(cmdArr[5]), Integer.parseInt(cmdArr[6]), cmdArr[7]);
                        System.out.println("success");
                    }
                } else {
                    System.out.println("permission-denied");
                }
            } else {
                System.out.println("invalid-pass");
            }
        }
    }

    public void libraryReport() {
        int numOfBooks = 0, numOfTheses = 0, numOfGanjinehBooks = 0, numOfSellingBooks = 0, numOfBorrowedBooks = 0, numOfBorrowedTheses = 0;
        for (Resource resource : resources.values()) {
            if (resource instanceof Book) {
                numOfBooks += ((Book) resource).getNumberAvailable();
            } else if (resource instanceof Thesis && ((Thesis) resource).getIsAvailable()) {
                numOfTheses++;
            } else if (resource instanceof GanjinehBook) {
                numOfGanjinehBooks++;
            } else if (resource instanceof SellingBook) {
                numOfSellingBooks += ((SellingBook) resource).getNumber();
            }
        }
        for (Borrowing borrowing : borrowings.values()) {
            if (borrowing.getResource() instanceof Book) {
                numOfBorrowedBooks++;
            } else if (borrowing.getResource() instanceof Thesis) {
                numOfBorrowedTheses++;
            }
        }
        System.out.println(numOfBooks + " " + numOfTheses + " " + numOfBorrowedBooks + " " + numOfBorrowedTheses + " " + numOfGanjinehBooks + " " + numOfSellingBooks);
    }

    public void reportPassedDeadline(String date, String time) {
        ArrayList<String> founds = new ArrayList<>();
        for (Borrowing borrowing : borrowings.values()) {
            long hourDiff = Borrowing.dateTimesDiffToHour(borrowing.getBorrowDateTime(), LocalDateTime.from(DateTimeFormatter.ofPattern("y-M-d-H:m").parse(date + "-" + time)));
            long minuteDiff = Borrowing.dateTimesDiffToHour(borrowing.getBorrowDateTime(), LocalDateTime.from(DateTimeFormatter.ofPattern("y-M-d-H:m").parse(date + "-" + time))) - hourDiff * 60 ;
            if (borrowing.getResource() instanceof Book) {
                if (borrowing.getBorrower() instanceof Student &&  (hourDiff> Borrower.MAX_HOURS_BOOK_STUDENT || hourDiff == Borrower.MAX_HOURS_BOOK_STUDENT && minuteDiff > 0)&& !founds.contains(borrowing.getResource().getId())) {
                    founds.add(borrowing.getResource().getId());
                } else if ((borrowing.getBorrower() instanceof Staff || borrowing.getBorrower() instanceof Professor || borrowing.getBorrower() instanceof Manager) && (hourDiff > Borrower.MAX_HOURS_BOOK_STAFF || hourDiff == Borrower.MAX_HOURS_BOOK_STAFF && minuteDiff > 0)  && !founds.contains(borrowing.getResource().getId())) {
                    founds.add(borrowing.getResource().getId());
                }
            } else if (borrowing.getResource() instanceof Thesis) {
                if (borrowing.getBorrower() instanceof Student && (hourDiff > Borrower.MAX_HOURS_THESIS_STUDENT || hourDiff == Borrower.MAX_HOURS_THESIS_STUDENT && minuteDiff > 0) && !founds.contains(borrowing.getResource().getId()) ) {
                    founds.add(borrowing.getResource().getId());
                } else if ((borrowing.getBorrower() instanceof Staff || borrowing.getBorrower() instanceof Professor || borrowing.getBorrower() instanceof Manager) && (hourDiff > Borrower.MAX_HOURS_THESIS_STAFF  || hourDiff == Borrower.MAX_HOURS_THESIS_STAFF && minuteDiff > 0) && !founds.contains(borrowing.getResource().getId())) {
                    founds.add(borrowing.getResource().getId());
                }
            }
        }
        Collections.sort(founds);
        if (founds.isEmpty()) {
            System.out.println("none");
        } else {
            Iterator<String> iterator = founds.iterator();
            String current = null;
            while (iterator.hasNext()) {
                String temp = iterator.next();
                if (current != null) {
                    System.out.print(current + "|");
                }
                current = temp;
            }
            System.out.println(current);
        }
    }
}