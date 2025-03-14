abstract class Person {
    private final String id;
    private final String password;

    public Person(String id, String password) {
        this.id = id;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

}

class Admin extends Person {

    public Admin(String id, String password) {
        super(id, password);
        LibraryIMS.people.put(this.getId(), this);
    }
}