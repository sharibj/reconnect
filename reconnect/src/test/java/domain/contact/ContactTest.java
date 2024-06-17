package domain.contact;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;

import domain.contact.Contact;

class ContactTest {

    @Test
    void whenContactIsCreatedWithoutNick_thenThrowException() {
        // when
        Contact.ContactBuilder contactBuilder = Contact.builder();

        // then
        assertThrows(
                NullPointerException.class,
                contactBuilder::build
        );
    }

    @Test
    void whenContactIsCreatedWithJustNickname_thenADefaultIdIsAssigned() {
        // when
        Contact contact = Contact.builder().nickName("sharib").build();

        //then
        assertEquals("sharib", contact.getNickName());
    }
}