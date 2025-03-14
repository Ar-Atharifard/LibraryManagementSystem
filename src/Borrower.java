interface Borrower {
    int MAX_SOURCES_STUDENT = 3;
    int MAX_HOURS_BOOK_STUDENT = 240;
    int MAX_HOURS_THESIS_STUDENT = 7 * 24;
    int MAX_SOURCES_STAFF = 5;
    int MAX_HOURS_BOOK_STAFF = 14 * 24;
    int MAX_HOURS_THESIS_STAFF = 240;
    int STUDENT_PENALTY_FOR_AN_HOUR = 50;
    int STAFF_PENALTY_FOR_AN_HOUR = 100;

    public int getCountOfBorrowedSources();

    public boolean canBorrow(String libraryId, String resourceId, String date, String time);

    public void borrow(String libraryId, String sourceId, String date, String time);

    public void returns(String commanderId, String libraryId, String sourceId, String date, String time);

}