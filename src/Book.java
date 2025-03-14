
class Book extends Resource implements Borrowable {
    private final String authorName;
    private final String publisherName;
    private final String printYear;
    private final short number;
    private short numberAvailable;

    public Book(String id, String name, String authorName, String publisherName, String printYear, short number, Category category, Library library) {
        super(id, library, name, category);
        this.authorName = authorName;
        this.publisherName = publisherName;
        this.printYear = printYear;
        this.number = number;
        this.numberAvailable = number;
        library.getResources().put(this.getId(), this);
    }

    public short getNumberAvailable() {
        return numberAvailable;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public String getAuthorName() {
        return authorName;
    }

    public short getNumber() {
        return number;
    }

    public void setNumberAvailable(short numberAvailable) {
        this.numberAvailable = numberAvailable;
    }

    public static void addBook(String[] cmdArr) {
        if (checkErrorsForAddResource(cmdArr[1], cmdArr[2], cmdArr[3], cmdArr[9], cmdArr[10])) {
            new Book(cmdArr[3], cmdArr[4], cmdArr[5], cmdArr[6], cmdArr[7], Short.parseShort(cmdArr[8]), LibraryIMS.categories.get(cmdArr[9]), LibraryIMS.libraries.get(cmdArr[10]));
            System.out.println("success");
        }
    }
}