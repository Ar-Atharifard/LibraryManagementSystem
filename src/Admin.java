class Admin extends Person {

    public Admin(String id, String password) {
        super(id, password);
        LibraryIMS.people.put(this.getId(), this);
    }
}