package com.library.model;

public class IssuedBook {

    private int id;
    private int bookId;
    private String bookTitle;
    private String borrowerName;
    private String issueDate;

    public IssuedBook() {}

    public IssuedBook(int id,
                      int bookId,
                      String bookTitle,
                      String borrowerName,
                      String issueDate) {
        this.id = id;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.borrowerName = borrowerName;
        this.issueDate = issueDate;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getBookId() { return bookId; }
    public void setBookId(int bookId) { this.bookId = bookId; }

    public String getBookTitle() { return bookTitle; }
    public void setBookTitle(String bookTitle) { this.bookTitle = bookTitle; }

    public String getBorrowerName() { return borrowerName; }
    public void setBorrowerName(String borrowerName) { this.borrowerName = borrowerName; }

    public String getIssueDate() { return issueDate; }
    public void setIssueDate(String issueDate) { this.issueDate = issueDate; }
}