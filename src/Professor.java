class Professor extends User implements Buyer {

    public Professor(String id, String password, String name, String lastName, String nationalNumber, String yearOfBirth, String address) {
        super(id, password, name, lastName, nationalNumber, yearOfBirth, address);
        LibraryIMS.people.put(this.getId(), this);
    }

    @Override
    public boolean canBuy() {
        return this.getDebt() == 0;
    }
}