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
            .filter(
                contact -> contact.getGender() == gender
            )
            .count();
    }

    public Optional<Contact> determineOldestContact() {
        return contactList
                .stream()
                .min(Comparator.comparing(Contact::getDob));
    }

    public int calculateContactDaysOlderThan(Contact c1, Contact c2) throws ContactNotOlderException {
        if (c1.getDob().isAfter(c2.getDob())) {
            throw new ContactNotOlderException("Contact " + c1.getName() + " was born after " + c2.getName());
        }

        return (int) DAYS.between(c1.getDob(), c2.getDob());
    }

    public Optional<Contact> findContactByName(String name) {
        return contactList
                .stream()
                .filter(contact -> contact.getName().equals(name))
                .findFirst();
    }
}
