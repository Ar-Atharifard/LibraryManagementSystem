class Manager extends User {
    private final Library library;

    public Manager(String id, String password, String name, String lastName, String nationalNumber, String yearOfBirth, String address, Library library) {
        super(id, password, name, lastName, nationalNumber, yearOfBirth, address);
        this.library = library;
        LibraryIMS.people.put(this.getId(), this);
    }

    public Library getLibrary() {
        return library;
    }

    public static void addManager(String[] cmdArr) {
        if (checkErrorsForAddUser(cmdArr))
            if (LibraryIMS.libraries.containsKey(cmdArr[10])) {
                Manager manager = new Manager(cmdArr[3], cmdArr[4], cmdArr[5], cmdArr[6], cmdArr[7], cmdArr[8], cmdArr[9], LibraryIMS.libraries.get(cmdArr[10]));
                LibraryIMS.libraries.get(manager.library.getId()).getManagers().put(manager.getId(), manager);
                System.out.println("success");
            } else {
                System.out.println("not-found");
            }
    }
}