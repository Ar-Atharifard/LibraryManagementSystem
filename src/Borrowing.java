import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

class Borrowing {
    private final Resource resource;
    private final Library library;
    private final User borrower;
    private final LocalDateTime borrowDateTime;


    public Borrowing(Resource resource, Library library, User borrower, String date, String time) {
        this.resource = resource;
        this.library = library;
        this.borrower = borrower;
        borrowDateTime = LocalDateTime.from(DateTimeFormatter.ofPattern("y-M-d-H:m").parse(date + "-" + time));
    }

    public User getBorrower() {
        return borrower;
    }

    public Resource getResource() {
        return resource;
    }

    public Library getLibrary() {
        return library;
    }

    public LocalDateTime getBorrowDateTime() {
        return borrowDateTime;
    }

    public static long dateTimesDiffToHour(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        return dateTime1.until(dateTime2, ChronoUnit.HOURS);
    }

    public static long dateTimesDiffToMinute(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        return dateTime1.until(dateTime2, ChronoUnit.MINUTES);
    }
}