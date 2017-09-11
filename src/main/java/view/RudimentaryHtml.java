package view;

import addressbook.AddressbookService;
import addressbook.model.Contact;
import addressbook.model.ContactNotOlderException;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import library.LibraryService;
import library.model.Book;

import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class RudimentaryHtml {

    private final AddressbookService addressbookService;
    private final LibraryService libraryService;
    private final Map<String, Object> templateVars;

    public RudimentaryHtml(AddressbookService addressbookService, LibraryService libraryService) {
        this.addressbookService = addressbookService;
        this.libraryService = libraryService;
        this.templateVars = new HashMap<>();
    }

    public String getHtml() {
        assignFemaleCountVar();
        assignOldestContactVar();
        assignOlderContactVar();
        assignRentalSummaryVar();
        assignNotRentedBooksVar();
        assignRentalsVar();

        Writer writer = new StringWriter();
        MustacheFactory mf = new DefaultMustacheFactory();
        Mustache mustache = mf.compile("templates/rudimentary.mustache");
        mustache.execute(writer, templateVars);

        return writer.toString();

    }

    private void assignRentalsVar() {
        Map<String, String> rentals = new HashMap<>();
        libraryService.findRentedBooks()
            .forEach(book ->
                addressbookService
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

    private void assignNotRentedBooksVar() {
        templateVars.put(
            "notRented",
            String.join(
                ", ",
                libraryService.findNotRentedBooks()
                    .stream()
                    .map(Book::getTitle)
                    .collect(Collectors.toList())
            )
        );
    }

    private void assignRentalSummaryVar() {
        Map<String, Integer> rentalSummary = new HashMap<>();
        libraryService.summarizeRentalsPerContact().forEach((contactId, qtyBooks) -> {
            addressbookService.findContactById(contactId).ifPresent(contact -> {
                rentalSummary.put(contact.getName(), qtyBooks);
            });
        });
        templateVars.put("rentalSummary", rentalSummary.entrySet());
    }

    private void assignOlderContactVar() {
        Optional<Contact> olderContact = addressbookService.findContactByName("Jon");
        Optional<Contact> youngerContact = addressbookService.findContactByName("Paul");
        if (olderContact.isPresent() && youngerContact.isPresent()) {
            try {
                templateVars.put("daysOlder", addressbookService.calculateContactDaysOlderThan(olderContact.get(), youngerContact.get()));
            } catch (ContactNotOlderException e) {
                templateVars.put("daysOlder", e.getMessage());
            }
        }
    }

    private void assignFemaleCountVar() {
        templateVars.put("females", addressbookService.calculateFemaleCount());
    }

    private void assignOldestContactVar() {
        addressbookService.findOldestContact().ifPresent(contact -> templateVars.put("oldest", contact.getName()));
    }


}
