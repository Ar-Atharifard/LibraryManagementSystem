class Staff extends User implements Buyer {

    public Staff(String id, String password, String name, String lastName, String nationalNumber, String yearOfBirth, String address) {
        super(id, password, name, lastName, nationalNumber, yearOfBirth, address);
        LibraryIMS.people.put(this.getId(), this);
    }

    public boolean canBuy() {
        return getDebt() == 0;
    }

}

