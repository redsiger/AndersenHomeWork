package com.example.androidschool.andersenhomeworks.lesson6

interface ContactsListener {

    fun onItemClick(id: Int)

    fun onItemEdit(id: Int)

    fun onItemSave(contact: Contact)

    fun onItemDelete(id: Int)

    fun onItemCancel()

    fun getDefaultId(): Int

    fun getContact(id: Int): Contact

    fun getContacts(): List<Contact>

    fun addRepositoryListener(listener: RepositoryListener)

    fun removeRepositoryListener(listener: RepositoryListener)
}