package addressbook.model;

import com.google.common.collect.ImmutableList;

import java.util.List;

public class Addressbook {
    private final ImmutableList<Contact> contactList;

    public Addressbook(List<Contact> contactList) {
        this.contactList = ImmutableList.copyOf(contactList);
    }

    public ImmutableList<Contact> getContactList() {
        return contactList;
    }
}
