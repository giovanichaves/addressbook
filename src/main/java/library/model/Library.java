package library.model;

import com.google.common.collect.ImmutableList;

import java.util.List;

public class Library {
    private ImmutableList<Book> bookList;

    public Library(List<Book> bookList) {
        this.bookList = ImmutableList.copyOf(bookList);
    }

    public ImmutableList<Book> getBookList() {
        return bookList;
    }
}
