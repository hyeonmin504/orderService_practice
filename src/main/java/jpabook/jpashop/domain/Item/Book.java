package jpabook.jpashop.domain.Item;

import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.EnableMBeanExport;

@Entity
@DiscriminatorValue("B")
@Getter @Setter
public class Book extends Item {

    private String author;
    private String isbn;

    public static Book createBook(String name, int price, int stockQuantity, String author, String isbn){
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        book.setAuthor(author);
        book.setIsbn(isbn);
        return book;
    }

}
