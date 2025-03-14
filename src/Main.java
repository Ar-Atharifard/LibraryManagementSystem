
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Main {
    private static final Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {
        new Admin("admin", "AdminPass");
        new Category("null", "null", null);
        commandCompute();
    }

    private static void commandCompute() {
        String[] cmdArr = null;
        do {
            cmdArr = scan.nextLine().split("[#|]");
            switch (cmdArr[0]) {
                case "add-library" -> Library.addLibrary(cmdArr);
                case "add-category" -> Category.addCategory(cmdArr);
                case "add-student" -> Student.addStudent(cmdArr);
                case "add-staff" -> User.addStaffOrProfessor(cmdArr);
                case "add-manager" -> Manager.addManager(cmdArr);
                case "remove-user" -> User.removeUser(cmdArr);
                case "add-book" -> Book.addBook(cmdArr);
                case "add-thesis" -> Thesis.addThesis(cmdArr);
                case "add-ganjineh-book" -> GanjinehBook.addGanjinehBook(cmdArr);
                case "add-selling-book" -> SellingBook.addSellingBook(cmdArr);
                case "remove-resource" -> Resource.removeResource(cmdArr);
                case "borrow" -> User.borrowCmd(cmdArr);
                case "return" -> User.returnsCmd(cmdArr);
                case "buy" -> SellingBook.buyCmd(cmdArr);
                case "read" -> GanjinehBook.readCmd(cmdArr);
                case "search" -> Resource.search(cmdArr[1]);
                case "add-comment" -> Resource.addCommentCmd(cmdArr);
                case "search-user" -> User.searchUserCmd(cmdArr);
                case "category-report" -> LibraryIMS.categoryReportCmd(cmdArr);
                case "library-report" -> LibraryIMS.libraryReportCmd(cmdArr);
                case "report-passed-deadline" -> LibraryIMS.reportPassedDeadlineCmd(cmdArr);
                case "report-penalties-sum" -> LibraryIMS.reportPenaltiesSumCmd(cmdArr);
            }

        } while (!cmdArr[0].equals("finish"));
    }
}
