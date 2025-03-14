import java.util.HashMap;

abstract class LibraryIMS {
    public static final HashMap<String, Library> libraries = new HashMap<>();
    public static final HashMap<String, Category> categories = new HashMap<>();
    public static final HashMap<String, Person> people = new HashMap<>();

    public static void categoryReportCmd(String[] cmdArr) {
        if (libraries.containsKey(cmdArr[4]) && people.containsKey(cmdArr[1]) && categories.containsKey(cmdArr[3])) {
            Library library = libraries.get(cmdArr[4]);
            Person person = people.get(cmdArr[1]);
            Category category = categories.get(cmdArr[3]);
            if (person instanceof Manager && library.getManagers().containsKey(person.getId())) {
                if (person.getPassword().equals(cmdArr[2])) {
                    Category.categoryReport(category, library);
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

    public static void libraryReportCmd(String[] cmdArr) {
        if (libraries.containsKey(cmdArr[3]) && people.containsKey(cmdArr[1])) {
            Library library = libraries.get(cmdArr[3]);
            Person person = people.get(cmdArr[1]);
            if (person instanceof Manager && library.getManagers().containsKey(person.getId())) {
                if (person.getPassword().equals(cmdArr[2])) {
                    library.libraryReport();
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

    public static void reportPassedDeadlineCmd(String[] cmdArr) {
        if (libraries.containsKey(cmdArr[3]) && people.containsKey(cmdArr[1])) {
            Person person = people.get(cmdArr[1]);
            Library library = libraries.get(cmdArr[3]);
            if (person instanceof Manager && library.getManagers().containsKey(person.getId())) {
                if (person.getPassword().equals(cmdArr[2])) {
                    library.reportPassedDeadline(cmdArr[4], cmdArr[5]);
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

    public static void reportPenaltiesSumCmd(String[] cmdArr) {
        if (people.containsKey(cmdArr[1])) {
            if (people.get(cmdArr[1]).getPassword().equals(cmdArr[2])) {
                if (people.get(cmdArr[1]) instanceof Admin) {
                    reportPenaltiesSum();
                } else {
                    System.out.println("permission-denied");
                }
            } else {
                System.out.println("invalid-pass");
            }
        } else {
            System.out.println("not-found");
        }
    }

    private static void reportPenaltiesSum() {
        long penaltiesSum = 0;
        for (Person person : people.values()) {
            if (person instanceof User && ((User) person).getDebt() > 0) {
                penaltiesSum += ((User) person).getDebt();
            }
        }
        System.out.println(penaltiesSum);
    }
}