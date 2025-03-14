import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

class Resource {

    private final String id;
    private final String name;
    private final Library library;
    private final Category category;
    private final HashMap<String, String> comments = new HashMap<>();

    public Resource(String id, Library library, String name, Category category) {
        this.id = id;
        this.library = library;
        this.category = category;
        this.name = name;}

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Library getLibrary() {
        return library;
    }

    public Category getCategory() {
        return category;
    }

    public boolean isAvailableForBorrow() {
        return (this instanceof Book && (((Book) this).getNumberAvailable() > 0) || this instanceof Thesis && ((Thesis) this).getIsAvailable());
    }

    protected static boolean checkErrorsForAddResource(String commanderId, String commanderPassword, String resourceId, String categoryId, String libraryId) {
        boolean noErrors = false;
        if (LibraryIMS.people.containsKey(commanderId) && LibraryIMS.libraries.containsKey(libraryId) && LibraryIMS.categories.containsKey(categoryId)) {
            Person commander = LibraryIMS.people.get(commanderId);
            if (commander instanceof Manager) {
                if (commander.getPassword().equals(commanderPassword)) {
                    if (((Manager) commander).getLibrary().getId().equals(libraryId)) {
                        if (LibraryIMS.libraries.get(libraryId).getResources().containsKey(resourceId)) {
                            System.out.println("duplicate-id");
                        } else {
                            noErrors = true;
                        }
                    } else {
                        System.out.println("permission-denied");
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
        return noErrors;
    }

    public static void removeResource(String[] cmdArr) {
        if (LibraryIMS.people.containsKey(cmdArr[1]) && LibraryIMS.libraries.containsKey(cmdArr[4]) && LibraryIMS.libraries.get(cmdArr[4]).getResources().containsKey(cmdArr[3])) {
            Person commander = LibraryIMS.people.get(cmdArr[1]);
            Resource resource = LibraryIMS.libraries.get(cmdArr[4]).getResources().get(cmdArr[3]);
            if (commander instanceof Manager) {
                if (commander.getPassword().equals(cmdArr[2])) {
                    if (((Manager) commander).getLibrary().getId().equals(cmdArr[4])) {
                        if (resource instanceof Book && ((Book) resource).getNumberAvailable() == ((Book) resource).getNumber() || resource instanceof Thesis && ((Thesis) resource).getIsAvailable() || resource instanceof GanjinehBook || resource instanceof SellingBook) {
                            LibraryIMS.libraries.get(cmdArr[4]).getResources().remove(cmdArr[3]);
                            System.out.println("success");
                        } else {
                            System.out.println("not-allowed");
                        }
                    } else {
                        System.out.println("permission-denied");
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

    private void addComment(String writer, String comment) {
        comments.put(writer, comment);
    }

    public static void addCommentCmd(String[] cmdArr) {
        if (LibraryIMS.people.containsKey(cmdArr[1]) && LibraryIMS.libraries.containsKey(cmdArr[3]) && LibraryIMS.libraries.get(cmdArr[3]).getResources().containsKey(cmdArr[4])) {
            Person commander = LibraryIMS.people.get(cmdArr[1]);
            Library library = LibraryIMS.libraries.get(cmdArr[3]);
            Resource resource = library.getResources().get(cmdArr[4]);
            if (commander instanceof Student || commander instanceof Professor) {
                if (commander.getPassword().equals(cmdArr[2])) {
                    resource.addComment(commander.getId(), cmdArr[5]);
                    System.out.println("success");
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

    public static void search(String searchExpression) {
        ArrayList<String> founds = new ArrayList<>();

        String expression = searchExpression.toLowerCase();
        for (Library library : LibraryIMS.libraries.values()) {
            for (Resource resource : library.getResources().values()) {
                if (resource.getName().toLowerCase().contains(expression) || resource instanceof GanjinehBook && (((GanjinehBook) resource).getAuthorName().toLowerCase().contains(expression) || ((GanjinehBook) resource).getPublisherName().toLowerCase().contains(expression)) || resource instanceof SellingBook && (((SellingBook) resource).getAuthorName().toLowerCase().contains(expression) || ((SellingBook) resource).getPublisherName().toLowerCase().contains(expression)) || resource instanceof Book && (((Book) resource).getAuthorName().toLowerCase().contains(expression) || ((Book) resource).getPublisherName().toLowerCase().contains(expression)) || resource instanceof Thesis && (((Thesis) resource).getAuthorName().toLowerCase().contains(expression) || ((Thesis) resource).getProfessorName().toLowerCase().contains(expression))) {
                    founds.add(resource.getId());
                }
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
}
