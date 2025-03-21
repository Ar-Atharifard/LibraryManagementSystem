class Student extends User implements Buyer {

    public Student(String id, String password, String name, String lastName, String nationalNumber, String yearOfBirth, String address) {
        super(id, password, name, lastName, nationalNumber, yearOfBirth, address);
        LibraryIMS.people.put(this.getId(), this);
    }

    public static void addStudent(String[] cmdArr) {
        if (checkErrorsForAddUser(cmdArr)) {
            new Student(cmdArr[3], cmdArr[4], cmdArr[5], cmdArr[6], cmdArr[7], cmdArr[8], cmdArr[9]);
            System.out.println("success");
        }
    }

    @Override
    public boolean canBuy() {
        return getDebt() == 0;
    }
}

