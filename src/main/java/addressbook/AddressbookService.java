package addressbook;

import addressbook.model.Addressbook;
import addressbook.model.Contact;
import addressbook.model.ContactNotOlderException;
import addressbook.model.Gender;

import java.util.Comparator;
import java.util.Optional;

import static java.time.temporal.ChronoUnit.DAYS;

public class AddressbookService {

    private final Addressbook addressbook;

    public AddressbookService(Addressbook addressbook) {
        this.addressbook = addressbook;
    }

    public int calculateFemaleCount() {
        return calculateGenderCount(Gender.FEMALE);
    }

    private int calculateGenderCount(Gender gender) {
        return (int) addressbook.getContactList()
            .stream()
            .filter(contact -> contact.getGender() == gender)
            .count();
    }

    public Optional<Contact> findOldestContact() {
        return addressbook.getContactList()
            .stream()
            .min(Comparator.comparing(Contact::getDob));
    }

    public int calculateContactDaysOlderThan(Contact olderContact, Contact youngerContact) throws ContactNotOlderException {
        if (olderContact.getDob().isAfter(youngerContact.getDob())) {
            throw new ContactNotOlderException("Contact " + olderContact.getName() + " was born after " + youngerContact.getName());
        }

        return (int) DAYS.between(olderContact.getDob(), youngerContact.getDob());
    }

    public Optional<Contact> findContactByName(String name) {
        return addressbook.getContactList()
            .stream()
            .filter(contact -> contact.getName().equals(name))
            .findFirst();
    }

    public Optional<Contact> findContactById(Integer id) {
        return addressbook.getContactList()
            .stream()
            .filter(contact -> contact.getId() == id)
            .findFirst();
    }
}
