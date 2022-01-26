package com.example.androidschool.andersenhomeworks.lesson6

import com.github.javafaker.Faker

interface ContactsDataSource {

    companion object {
        private const val DEFAULT_LIST_SIZE = 100
        const val DEFAULT_CONTACT_ID = -1
    }

    fun getDefaultId(): Int
    fun getContacts(): List<Contact>
    fun getContact(id: Int): Contact
    fun deleteContact(id: Int)
    fun editContact(contact: Contact)
    fun searchContacts(query: String): List<Contact>

    class Local: ContactsDataSource {

        private val faker = Faker()

        private var _contactsList = populateContacts(DEFAULT_LIST_SIZE)
        private val contactList: List<Contact> get() = _contactsList.toList()


        override fun getDefaultId(): Int = contactList.first().id

        override fun getContacts() = contactList

        override fun getContact(id: Int): Contact {
            return try {
                if (id != DEFAULT_CONTACT_ID) contactList.first { it.id == id }
                else Contact(id = DEFAULT_CONTACT_ID)
            } catch (e: NoSuchElementException) {
                Contact(id = DEFAULT_CONTACT_ID)
            }
        }

        override fun deleteContact(id: Int) {
            _contactsList = _contactsList.filterNot { it.id == id }
        }

        override fun editContact(contact: Contact) {
            _contactsList = _contactsList.map { if (it.id == contact.id) contact else it }
        }

        override fun searchContacts(query: String): List<Contact> {
            return contactList.filter {
                it.firstName.contains(query, true) || it.lastName.contains(query, true)
            }
        }

        private fun populateContacts(contactsCount: Int): List<Contact> {
            return buildList {
                for (i in 0 until contactsCount)
                    add(Contact(
                            id = i,
                            firstName = faker.name().firstName(),
                            lastName = faker.name().lastName(),
                            phoneNumber = faker.phoneNumber().phoneNumber(),
                            imgUrl = "https://picsum.photos/200?temp=" + faker.name()
                    ))
            }
        }
    }
}