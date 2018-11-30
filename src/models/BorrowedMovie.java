package models;

public class BorrowedMovie extends Movie {

    private Borrowed borrowed;

    public String getDueDate(){
        return this.borrowed.getDueDate();
    }

    public Borrowed getBorrowed() {
        return borrowed;
    }

    public void setBorrowed(Borrowed borrowed) {
        this.borrowed = borrowed;
    }

    public BorrowedMovie(Borrowed borrowed) {
        this.borrowed = borrowed;
    }

    public BorrowedMovie(Long id, String name, String description, Integer yearOfRelease, Boolean forAdults, Double price, Borrowed borrowed) {
        super(id, name, description, yearOfRelease, forAdults, price);
        this.borrowed = borrowed;
    }
}
