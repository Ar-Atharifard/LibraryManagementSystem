import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

abstract class User extends Person implements Borrower {

    private final String name;
    private final String lastName;
    private final String nationalNumber;
    private final String yearOfBirth;
    private final String address;
    private long debt;
    private final HashMap<String, Borrowing> borrowings;

    public User(String id, String password, String name, String lastName, String nationalNumber, String yearOfBirth, String address) {
        super(id, password);
        this.name = name;
        this.lastName = lastName;
        this.nationalNumber = nationalNumber;
        this.yearOfBirth = yearOfBirth;
        this.address = address;
        debt = 0;
        borrowings = new HashMap<>();
    }

    @Override
    public int getCountOfBorrowedSources() {
        return borrowings.size();
    }

    public String getLastName() {
        return lastName;
    }

    public String getName() {
        return name;
    }

    public HashMap<String, Borrowing> getBorrowings() {
        return borrowings;
    }

    public long getDebt() {
        return debt;
    }

    public void setDebt(long debt) {
        this.debt = debt;
    }

    @Override
    public void borrow(String libraryId, String sourceId, String date, String time) {
        Borrowing borrowing = new Borrowing(LibraryIMS.libraries.get(libraryId).getResources().get(sourceId), LibraryIMS.libraries.get(libraryId), this, date, time);
        this.borrowings.put(libraryId + " " + sourceId, borrowing);
        LibraryIMS.libraries.get(libraryId).getBorrowings().put(this.getId() + " " + sourceId, borrowing);
        if (borrowing.getResource() instanceof Book book) {
            book.setNumberAvailable((short) (book.getNumberAvailable() - 1));
        } else if (borrowing.getResource() instanceof Thesis thesis) {
            thesis.setAvailable(false);
        }
    }

    public static void borrowCmd(String[] cmdArr) {
        if (LibraryIMS.people.containsKey(cmdArr[1]) && LibraryIMS.libraries.containsKey(cmdArr[3]) && LibraryIMS.libraries.get(cmdArr[3]).getResources().containsKey(cmdArr[4])) {
            Library library = LibraryIMS.libraries.get(cmdArr[3]);
            Resource resource = library.getResources().get(cmdArr[4]);
            Person commander = LibraryIMS.people.get(cmdArr[1]);
            if (commander.getPassword().equals(cmdArr[2])) {
                if (commander instanceof Borrower) {
                    if (resource instanceof Borrowable) {
                        if (((User) commander).canBorrow(cmdArr[3], cmdArr[4], cmdArr[5], cmdArr[6]) && resource.isAvailableForBorrow()) {
                            ((User) commander).borrow(cmdArr[3], cmdArr[4], cmdArr[5], cmdArr[6]);
                            System.out.println("success");
                        } else {
                            System.out.println("not-allowed");
                        }
                    } else {
                        System.out.println("not-allowed");
                    }
                }
            } else {
                System.out.println("invalid-pass");
            }
        } else {
            System.out.println("not-found");
        }
    }

    public void returns(String commanderId, String libraryId, String sourceId, String date, String time) {
        LibraryIMS.libraries.get(libraryId).getBorrowings().remove(commanderId + " " + sourceId);
        if (LibraryIMS.people.get(commanderId) instanceof User) {
            User borrower = (User) (LibraryIMS.people.get(commanderId));
            Resource source = borrowings.get(libraryId + " " + sourceId).getResource();
            long debt = 0;
            long hourDiff = Borrowing.dateTimesDiffToHour(borrower.borrowings.get(libraryId + " " + sourceId).getBorrowDateTime(), LocalDateTime.from(DateTimeFormatter.ofPattern("y-M-d-H:m").parse(date + "-" + time)));
            long minuteDiff = Borrowing.dateTimesDiffToMinute(borrower.borrowings.get(libraryId + " " + sourceId).getBorrowDateTime(), LocalDateTime.from(DateTimeFormatter.ofPattern("y-M-d-H:m").parse(date + "-" + time))) - hourDiff * 60;
            if(minuteDiff > 0 ){
                hourDiff ++;
            }
            if (borrower instanceof Student) {
                if (source instanceof Book) {
                    ((Book) source).setNumberAvailable((short) (((Book) source).getNumberAvailable() + 1));
                    if (hourDiff > Borrower.MAX_HOURS_BOOK_STUDENT) {
                        debt = (hourDiff - Borrower.MAX_HOURS_BOOK_STUDENT) * Borrower.STUDENT_PENALTY_FOR_AN_HOUR;
                    }
                } else if (source instanceof Thesis) {
                    ((Thesis) source).setAvailable(true);
                    if (hourDiff > Borrower.MAX_HOURS_THESIS_STUDENT) {
                        debt = (hourDiff - Borrower.MAX_HOURS_THESIS_STUDENT) * Borrower.STUDENT_PENALTY_FOR_AN_HOUR;
                    }
                }
            } else if (borrower instanceof Staff || borrower instanceof Professor || borrower instanceof Manager) {
                if (source instanceof Book) {
                    ((Book) source).setNumberAvailable((short) (((Book) source).getNumberAvailable() + 1));
                    if (hourDiff > Borrower.MAX_HOURS_BOOK_STAFF) {
                        debt = Borrower.STAFF_PENALTY_FOR_AN_HOUR * (hourDiff - Borrower.MAX_HOURS_BOOK_STAFF);
                    }
                } else if (source instanceof Thesis) {
                    ((Thesis) source).setAvailable(true);
                    if (hourDiff > Borrower.MAX_HOURS_THESIS_STAFF) {
                        debt = Borrower.STAFF_PENALTY_FOR_AN_HOUR * (hourDiff - Borrower.MAX_HOURS_THESIS_STAFF);
                    }
                }
            }
            this.debt += debt;
            borrower.borrowings.remove(libraryId + " " + sourceId);
            if (debt == 0) {
                System.out.println("success");
            } else {
                System.out.println(debt);
            }
        }
    }

    public static void returnsCmd(String[] cmdArr) {
        if (LibraryIMS.people.containsKey(cmdArr[1]) && LibraryIMS.libraries.containsKey(cmdArr[3]) && LibraryIMS.libraries.get(cmdArr[3]).getResources().containsKey(cmdArr[4]) && LibraryIMS.people.get(cmdArr[1]) instanceof User borrower && ((User) LibraryIMS.people.get(cmdArr[1])).borrowings.containsKey(cmdArr[3] + " " + cmdArr[4]) && LibraryIMS.libraries.get(cmdArr[3]).getBorrowings().containsKey(borrower.getId() + " " + cmdArr[4])) {
            if (LibraryIMS.people.get(cmdArr[1]).getPassword().equals(cmdArr[2])) {
                borrower.returns(cmdArr[1], cmdArr[3], cmdArr[4], cmdArr[5], cmdArr[6]);
            } else {
                System.out.println("invalid-pass");
            }
        } else {
            System.out.println("not-found");
        }
    }

    @Override
    public boolean canBorrow(String libraryId, String resourceId, String date, String time) {
        User commander = this;
        boolean canBorrow = true;
        if ((commander instanceof Student && (commander.getCountOfBorrowedSources() < commander.MAX_SOURCES_STUDENT) || (commander instanceof Staff || commander instanceof Manager || commander instanceof Professor) && (commander.getCountOfBorrowedSources() < commander.MAX_SOURCES_STAFF)) && debt == 0) {
            if (commander.getBorrowings().containsKey(libraryId + " " + resourceId)) {
                canBorrow = false;
            }
        } else {
            canBorrow = false;
        }
        return canBorrow;
    }

    protected static boolean checkErrorsForAddUser(String[] cmdArr) {
        boolean noError = false;
        if (LibraryIMS.people.containsKey(cmdArr[1])) {
            if (LibraryIMS.people.get(cmdArr[1]).getPassword().equals(cmdArr[2])) {
                if (LibraryIMS.people.get(cmdArr[1]) instanceof Admin) {
                    if (LibraryIMS.people.containsKey(cmdArr[3])) {
                        System.out.println("duplicate-id");
                    } else {
                        noError = true;
                    }
                } else {
                    System.out.println("permission-denied");
                }
            } else {
                System.out.println("invalid-pass");
            }
        } else {
            System.out.println("not-found");
        }
        return noError;
    }

    public static void addStaffOrProfessor(String[] cmdArr) {
        if (checkErrorsForAddUser(cmdArr)) {
            if (cmdArr[10].equals("staff")) {
                new Staff(cmdArr[3], cmdArr[4], cmdArr[5], cmdArr[6], cmdArr[7], cmdArr[8], cmdArr[9]);
                System.out.println("success");
            } else if (cmdArr[10].equals("professor")) {
                new Professor(cmdArr[3], cmdArr[4], cmdArr[5], cmdArr[6], cmdArr[7], cmdArr[8], cmdArr[9]);
                System.out.println("success");
            }
        }
    }

    public static void removeUser(String[] cmdArr) {
        if (LibraryIMS.people.containsKey(cmdArr[1]) && LibraryIMS.people.get(cmdArr[3]) instanceof User) {
            Person commander = LibraryIMS.people.get(cmdArr[1]);
            if (commander instanceof Admin) {
                if (commander.getPassword().equals(cmdArr[2])) {
                    if (LibraryIMS.people.containsKey(cmdArr[3]) && LibraryIMS.people.get(cmdArr[3]) instanceof User) {
                        if (((User) LibraryIMS.people.get(cmdArr[3])).getBorrowings().isEmpty() && ((User) LibraryIMS.people.get(cmdArr[3])).getDebt() == 0) {
                            if (LibraryIMS.people.get(cmdArr[3]) instanceof Manager manager) {
                                manager.getLibrary().getManagers().remove(cmdArr[3]);
                            }
                            LibraryIMS.people.remove(cmdArr[3]);
                            System.out.println("success");
                        } else {
                            System.out.println("not-allowed");
                        }
                    } else {
                        System.out.println("not-found");
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

    private static void searchUser(String searchExpression) {
        String expression = searchExpression.toLowerCase();
        ArrayList<String> founds = new ArrayList<>();
        for (Person person : LibraryIMS.people.values()) {
            if (person instanceof User && (((User) person).getName().toLowerCase().contains(expression) || ((User) person).getLastName().toLowerCase().contains(expression))) {
                founds.add(person.getId());
            }
        }
        Collections.sort(founds);
        if (founds.isEmpty()) {
            System.out.println("not-found");
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

    public static void searchUserCmd(String[] cmdArr) {
        if (LibraryIMS.people.containsKey(cmdArr[1])) {
            if (LibraryIMS.people.get(cmdArr[1]) instanceof Staff || LibraryIMS.people.get(cmdArr[1]) instanceof Professor || LibraryIMS.people.get(cmdArr[1]) instanceof Manager) {
                if (LibraryIMS.people.get(cmdArr[1]).getPassword().equals(cmdArr[2])) {
                    searchUser(cmdArr[3]);
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

}