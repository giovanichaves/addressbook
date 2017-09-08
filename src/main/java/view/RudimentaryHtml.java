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

    private final Addressbook addressbook;
    private final Library library;

    public RudimentaryHtml(Addressbook addressbook, Library library) {
        this.addressbook = addressbook;
        this.library = library;
    }

    public String getHtml() throws IOException {
        HashMap<String, Object> templateVars = new HashMap<>();

        assignFemaleCount(templateVars);

        assignOldestContact(templateVars);

        assignOlderContact(templateVars);

        assignRentalSummary(templateVars);

        assignNotRentedBooks(templateVars);

        assignRentals(templateVars);

        Writer writer = new StringWriter();
        MustacheFactory mf = new DefaultMustacheFactory();
        Mustache mustache = mf.compile("templates/rudimentary.mustache");
        mustache.execute(writer, templateVars);

        return writer.toString();

    }

    private void assignRentals(HashMap<String, Object> templateVars) {
        Map<String, String> rentals = new HashMap<>();
        library.findRentedBooks()
            .forEach(book ->
                addressbook
                    .findContactById(book.getRenter())
                    .ifPresent(renter -> {

                        if (!rentals.containsKey(renter.getName())) {
                            rentals.put(
                                    renter.getName(),
                                    book.getTitle()
                            );
                        } else {
                            rentals.put(
                                    renter.getName(),
                                    rentals.get(renter.getName()) + ", " + book.getTitle()
                            );
                        }
                    })
            );

        templateVars.put("rentals", rentals.entrySet());
    }

    private void assignNotRentedBooks(HashMap<String, Object> templateVars) {
        templateVars.put(
            "notRented",
            String.join(
                ", ",
                library.determineNotRentedBooks()
                    .stream()
                    .map(Book::getTitle)
                    .collect(Collectors.toList())
            )
        );
    }

    private void assignRentalSummary(HashMap<String, Object> templateVars) {
        Map<String, Integer> rentalSummary = new HashMap<>();
        library.summarizeRentalsPerContact().forEach((contactId, qtyBooks) -> {
            addressbook.findContactById(contactId).ifPresent(contact -> {
                rentalSummary.put(contact.getName(), qtyBooks);
            });
        });
        templateVars.put("rentalSummary", rentalSummary.entrySet());
    }

    private void assignOlderContact(HashMap<String, Object> templateVars) {
        Optional<Contact> olderContact = addressbook.findContactByName("Jon");
        Optional<Contact> youngerContact = addressbook.findContactByName("Paul");
        if (olderContact.isPresent() && youngerContact.isPresent()) {
            try {
                templateVars.put("daysOlder", addressbook.calculateContactDaysOlderThan(olderContact.get(), youngerContact.get()));
            } catch (ContactNotOlderException e) {
                templateVars.put("daysOlder", e.getMessage());
            }
        }
    }

    private void assignFemaleCount(HashMap<String, Object> templateVars) {
        templateVars.put("females", addressbook.calculateFemaleCount());
    }

    private void assignOldestContact(HashMap<String, Object> templateVars) {
        addressbook.determineOldestContact().ifPresent(contact -> templateVars.put("oldest", contact.getName()));
    }
}
