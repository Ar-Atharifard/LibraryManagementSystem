class Thesis extends Resource implements Borrowable {
    private final String authorName;
    private final String professorName;
    private final String defenceYear;
    private boolean isAvailable;

    public Thesis(String id, String name, String authorName, String professorName, String defenceYear, Category category, Library library) {
        super(id, library, name, category);
        this.authorName = authorName;
        this.professorName = professorName;
        this.defenceYear = defenceYear;
        this.isAvailable = true;
        library.getResources().put(this.getId(), this);
        library.getResources().put(this.getId(), this);
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getProfessorName() {
        return professorName;
    }

    public boolean getIsAvailable() {
        return isAvailable;
    }

    public static void addThesis(String[] cmdArr) {
        if (checkErrorsForAddResource(cmdArr[1], cmdArr[2], cmdArr[3], cmdArr[8], cmdArr[9])) {
            new Thesis(cmdArr[3], cmdArr[4], cmdArr[5], cmdArr[6], cmdArr[7], LibraryIMS.categories.get(cmdArr[8]), LibraryIMS.libraries.get(cmdArr[9]));
            System.out.println("success");
        }
    }
}
