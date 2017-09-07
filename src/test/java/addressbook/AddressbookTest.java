package addressbook;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class AddressbookTest {

    private List<Contact> contactList;
    private Addressbook addressbook;

    @Before
    public void setUp() {
        contactList = Arrays.asList(
                new Contact(1, "Test1", Gender.FEMALE, LocalDate.parse("1995-07-24")),
                new Contact(2, "Test2", Gender.FEMALE, LocalDate.parse("1995-07-26")),
                new Contact(3, "Test3", Gender.MALE, LocalDate.parse("1985-05-23")),
                new Contact(4, "Test4", Gender.MALE, LocalDate.parse("1980-04-22")),
                new Contact(5, "Test5", Gender.FEMALE, LocalDate.parse("1975-03-21"))
        );

        addressbook = new Addressbook(contactList);
    }

    @Test
    public void testCalculateFemaleCount() {
        Assert.assertEquals(3, addressbook.calculateFemaleCount());
    }

    @Test
    public void testDetermineOldestContact() {
        Assert.assertEquals(contactList.get(4), addressbook.determineOldestContact().get());
    }

    @Test
    public void testNoOldestContactDetermined() {
        Addressbook emptyAddressbook = new Addressbook(Collections.emptyList());
        Assert.assertEquals(Optional.empty(), emptyAddressbook.determineOldestContact());
    }

    @Test
    public void testCalculateDaysBetweenContacts() throws ContactNotOlderException {
        Assert.assertEquals(2, addressbook.calculateContactDaysOlderThan(contactList.get(0), contactList.get(1)));
    }

    @Test(expected = ContactNotOlderException.class)
    public void testExceptionWhenContactIsNotOlder() throws ContactNotOlderException {
        addressbook.calculateContactDaysOlderThan(contactList.get(1), contactList.get(0));
    }


}
