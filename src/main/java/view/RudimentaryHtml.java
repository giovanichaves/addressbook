package view;

import addressbook.Addressbook;
import addressbook.Contact;
import addressbook.ContactNotOlderException;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import library.Book;
import library.Library;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class RudimentaryHtml {

    public String getHtml(Addressbook addressbook, Library library) throws IOException, ContactNotOlderException {
        HashMap<String, Object> scopes = new HashMap<>();

        scopes.put("females", addressbook.calculateFemaleCount());
        Optional<Contact> oldest = addressbook.determineOldestContact();
        oldest.ifPresent(contact -> scopes.put("oldest", contact.getName()));

        Optional<Contact> c1 = addressbook.findContactByName("Jon");
        Optional<Contact> c2 = addressbook.findContactByName("Paul");
        if (c1.isPresent() && c2.isPresent()) {
            scopes.put("daysOlder", addressbook.calculateContactDaysOlderThan(c1.get(), c2.get()));
        }


        Map<Integer, Integer> rentalSummary = library.summarizeRentalsPerPerson();

        scopes.put(
            "notRented",
            library.determineNotRentedBooks()
                .stream()
                .map(Book::getTitle)
                .collect(Collectors.toList())
        );

        Writer writer = new StringWriter();
        MustacheFactory mf = new DefaultMustacheFactory();
        Mustache mustache = mf.compile("templates/rudimentary.mustache");
        mustache.execute(writer, scopes);

        return writer.toString();

    }
}
