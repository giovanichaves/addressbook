package addressbook;

import com.google.common.collect.ImmutableList;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static java.time.temporal.ChronoUnit.DAYS;

public class Addressbook {

    private ImmutableList<Contact> contactList;

    public Addressbook(List<Contact> contactList) {
        this.contactList = ImmutableList.copyOf(contactList);
    }

    public int calculateFemaleCount() {
        return calculateGenderCount(Gender.FEMALE);
    }

    private int calculateGenderCount(Gender gender) {
        return (int) contactList
            .stream()
            .filter(contact -> contact.getGender() == gender)
            .count();
    }

    public Optional<Contact> determineOldestContact() {
        return contactList
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
        return contactList
                .stream()
                .filter(contact -> contact.getName().equals(name))
                .findFirst();
    }

    public Optional<Contact> findContactById(Integer id) {
        return contactList
                .stream()
                .filter(contact -> contact.getId() == id)
                .findFirst();
    }
}
