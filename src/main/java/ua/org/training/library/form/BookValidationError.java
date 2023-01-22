package ua.org.training.library.form;


public class BookValidationError {
    private String name;
    private String count;
    private String ISBN;
    private String datePublication;
    private String finePerDay;
    private String authors;
    private boolean containsErrors = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.containsErrors = true;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
        this.containsErrors = true;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
        this.containsErrors = true;
    }

    public String getDatePublication() {
        return datePublication;
    }

    public void setDatePublication(String datePublication) {
        this.datePublication = datePublication;
        this.containsErrors = true;
    }

    public String getFinePerDay() {
        return finePerDay;
    }

    public void setFinePerDay(String finePerDay) {
        this.finePerDay = finePerDay;
        this.containsErrors = true;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
        this.containsErrors = true;
    }

    public boolean isContainsErrors() {
        return containsErrors;
    }
}
