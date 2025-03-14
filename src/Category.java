import java.util.ArrayList;

class Category {
    private final String id;
    private final String name;
    private final Category parentCategory;


    public Category(String id, String name, Category parentCategory) {
        this.id = id;
        this.name = name;
        this.parentCategory = parentCategory;
        LibraryIMS.categories.put(this.id, this);
    }

    public String getId() {
        return id;
    }


    public static void addCategory(String[] cmdArr) {
        if (LibraryIMS.people.containsKey(cmdArr[1])) {
            Person commander = LibraryIMS.people.get(cmdArr[1]);
            if (commander.getPassword().equals(cmdArr[2])) {
                if (commander instanceof Admin) {
                    if (LibraryIMS.categories.containsKey(cmdArr[3])) {
                        System.out.println("duplicate-id");
                    } else {
                        if (!cmdArr[5].equals("null") && !LibraryIMS.categories.containsKey(cmdArr[5])) {
                            System.out.println("not-found");
                        } else {
                            if (cmdArr[5].equals("null")) {
                                new Category(cmdArr[3], cmdArr[4], null);
                            } else {
                                new Category(cmdArr[3], cmdArr[4], LibraryIMS.categories.get(cmdArr[5]));
                            }
                            System.out.println("success");
                        }
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
    }

    public static void categoryReport(Category category, Library library) {
        int numOfBooks = 0, numOfTheses = 0, numOfSellingBooks = 0, numOfGanjinehBooks = 0;
        ArrayList<String> allRelatedCategories = new ArrayList<>();
        allRelatedCategories.add(category.id);
        boolean finished = false;
        while (!finished){
            finished = true;
            for (Category category1 : LibraryIMS.categories.values()) {
                if (category1.parentCategory != null && allRelatedCategories.contains(category1.parentCategory.id) && !allRelatedCategories.contains(category1.id)) {
                    allRelatedCategories.add(category1.id);
                    finished = false;
                }
            }
        }

        for (Resource resource : library.getResources().values()) {
            if(allRelatedCategories.contains(resource.getCategory().id)){
                if (resource instanceof Book) {
                    numOfBooks += ((Book) resource).getNumberAvailable();
                } else if (resource instanceof Thesis && ((Thesis) resource).getIsAvailable()) {
                    numOfTheses++;
                } else if (resource instanceof SellingBook) {
                    numOfSellingBooks += ((SellingBook) resource).getNumber();
                } else if (resource instanceof GanjinehBook) {
                    numOfGanjinehBooks++;
                }
            }
        }
        System.out.println(numOfBooks + " " + numOfTheses + " " + numOfGanjinehBooks + " " + numOfSellingBooks);
    }
}