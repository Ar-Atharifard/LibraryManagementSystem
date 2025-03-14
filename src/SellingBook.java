class SellingBook extends Resource {
    private final String authorName;
    private final String publisherName;
    private final String printYear;
    private final long price;
    private final short offerPercentage;
    private short number;

    public SellingBook(String id, String name, String authorName, String publisherName, String printYear, long price, short offerPercentage, short number, Category category, Library library) {
        super(id, library, name, category);
        this.authorName = authorName;
        this.publisherName = publisherName;
        this.printYear = printYear;
        this.price = price;
        this.offerPercentage = offerPercentage;
        this.number = number;
        library.getResources().put(this.getId(), this);
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public short getNumber() {
        return number;
    }

    public static void addSellingBook(String[] cmdArr) {
        if (checkErrorsForAddResource(cmdArr[1], cmdArr[2], cmdArr[3], cmdArr[11], cmdArr[12])) {
            new SellingBook(cmdArr[3], cmdArr[4], cmdArr[5], cmdArr[6], cmdArr[7], Long.parseLong(cmdArr[9]), Short.parseShort(cmdArr[10]), Short.parseShort(cmdArr[8]), LibraryIMS.categories.get(cmdArr[11]), LibraryIMS.libraries.get(cmdArr[12]));
            System.out.println("success");
        }
    }

    private void buy() {
        this.number--;
    }

    public static void buyCmd(String[] cmdArr) {
        if (LibraryIMS.people.containsKey(cmdArr[1]) && LibraryIMS.libraries.containsKey(cmdArr[3]) && LibraryIMS.libraries.get(cmdArr[3]).getResources().containsKey(cmdArr[4])) {
            Person person = LibraryIMS.people.get(cmdArr[1]);
            if (person instanceof Buyer) {
                if (person.getPassword().equals(cmdArr[2])) {
                    Library library = LibraryIMS.libraries.get(cmdArr[3]);
                    Resource resource = library.getResources().get(cmdArr[4]);
                    if (resource instanceof SellingBook && ((Buyer) person).canBuy() && ((SellingBook) resource).getNumber() > 0) {
                        ((SellingBook) resource).buy();
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
}
