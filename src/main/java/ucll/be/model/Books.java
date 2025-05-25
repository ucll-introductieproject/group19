package ucll.be.model;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("Book")
public class Books extends Publication {
    private String author;
    private String ISBN;

    public Books(){
        super();
    }

    public Books(String title, int year, int available, String author, String ISBN){
        super(title, year, available);
        setAuthor(author);
        setISBN(ISBN);
    }

    public void setAuthor(String author){
        if (author != null && !author.trim().isEmpty()){
            this.author = author;
        }else {throw new RuntimeException("Author can't be empty.");}
    }

    public void setISBN(String ISBN){
        if ((ISBN != null) && !ISBN.trim().isEmpty() && ISBN.replaceAll("-","").matches("\\d{13}")){
            this.ISBN = ISBN;
        }else {throw new RuntimeException("ISBN can't be empty and must contain exactly 13 digits.");}
    }

    public String getAuthor(){
        return author;
    }

    public String getISBN(){
        return ISBN;
    }

    @Override
    public String getType(){
        return "Book" + getTitle() + "Author: " + getAuthor() + "ISBN: " + getISBN();
    }
}
