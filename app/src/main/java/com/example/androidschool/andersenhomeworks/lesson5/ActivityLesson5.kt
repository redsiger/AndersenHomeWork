package com.example.androidschool.andersenhomeworks.lesson5

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.example.androidschool.andersenhomeworks.R
import com.example.androidschool.andersenhomeworks.databinding.ActivityLesson5Binding
import com.example.androidschool.andersenhomeworks.databinding.ActivityLesson5LandBinding
import com.example.androidschool.andersenhomeworks.lesson5.ContactDetailsFragment.Companion.CONTACT_DETAILS_FRAGMENT_TAG
import com.example.androidschool.andersenhomeworks.lesson5.ContactEditFragment.Companion.CONTACT_EDIT_FRAGMENT_TAG
import com.example.androidschool.andersenhomeworks.lesson5.ContactsListFragment.Companion.CONTACTS_LIST_FRAGMENT_TAG
import com.github.javafaker.Faker

class ActivityLesson5: AppCompatActivity(), FragmentListener {

    companion object {
        private const val CONTACT_LIST = "CONTACT_LIST"
    }

    private val isTablet: Boolean by lazy { this.resources.getBoolean(R.bool.isTablet) }
    private var _tabletBinding: ActivityLesson5LandBinding? = null
    private val tabletBinding get() = _tabletBinding!!

    private var _phoneBinding: ActivityLesson5Binding? = null
    private val phoneBinding get() = _phoneBinding!!

    private val manager: FragmentManager by lazy { supportFragmentManager }

    private val faker = Faker()
    private var contactList = mutableListOf<Contact>()
    private var currentContactId = 0
    private val repositoryListeners = mutableListOf<RepositoryListener>()

    override fun addRepositoryListener(listener: RepositoryListener) {
        repositoryListeners.add(listener)
    }

    private fun repositoryUpdated() {
        repositoryListeners.forEach {
            it.repositoryUpdated()
        }
    }

    override fun onItemClick(id: Int) {
        when {
            isTablet -> {
                if (id != currentContactId) {
                    currentContactId = id
                    replaceDetailFragment(contactList[currentContactId])
                }
            }
            else -> {
                addDetailFragment(id, R.id.fragment_container)
            }
        }
    }

    override fun onItemEdit(id: Int) {
        when {
            isTablet -> {
                addEditFragment(contactList[id])
            }
            else -> {
                addEditFragment(contactList[id])
            }
        }
    }

    override fun getContact(id: Int): Contact {
        return contactList[id]
    }

    override fun getContacts(): List<Contact> {
        return contactList
    }

    override fun onItemSave(contact: Contact) {
        contactList.removeAt(contact.id)
        contactList.add(contact.id, contact)
        removeEditFragment()
        repositoryUpdated()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()

        savedInstanceState?.let {
            contactList = it.getParcelableArrayList(CONTACT_LIST) ?: ArrayList()
        } ?: run {
            contactList = populateContacts(3)

            when {
                isTablet -> setTabletFragments()
                else -> setPhoneFragments()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList(CONTACT_LIST, ArrayList(contactList))
        super.onSaveInstanceState(outState)
    }


    private fun populateContacts(contactsCount: Int): ArrayList<Contact> {
        val contactList = ArrayList<Contact>()
        for (i in 0 until contactsCount) {
            val contact = Contact(
                id = i,
                firstName = faker.name().firstName(),
                secondName = faker.name().lastName(),
                phoneNumber = faker.phoneNumber().phoneNumber()
            )
            contactList.add(contact)
        }
        return contactList
    }

    private fun initView() {
        when {
            isTablet -> initTabletView()
            else -> initPhoneView()
        }
    }

    private fun initTabletView() {
        _tabletBinding = ActivityLesson5LandBinding.inflate(layoutInflater)
        setContentView(tabletBinding.root)
    }

    private fun setTabletFragments() {
        replaceListFragment(contactList, R.id.list_fragment)
        addDetailFragment(contactList[currentContactId].id, R.id.details_fragment)
    }

    private fun initPhoneView() {
        _phoneBinding = ActivityLesson5Binding.inflate(layoutInflater)
        setContentView(phoneBinding.root)
    }

    private fun setPhoneFragments() {
        replaceListFragment(contactList, R.id.fragment_container)
    }

    private fun replaceListFragment(contactList: List<Contact>, containerViewId: Int) {
        removeListFragment()
        addListFragment(containerViewId)
    }

    private fun removeListFragment() {
        manager.findFragmentByTag(CONTACTS_LIST_FRAGMENT_TAG)?.let { existFragment ->
            manager.beginTransaction()
                .remove(existFragment)
                .commit()
        }
    }

    private fun addListFragment(containerViewId: Int) {
        ContactsListFragment.newInstance().apply {
            manager.beginTransaction()
                .replace(
                    containerViewId,
                    this,
                    CONTACTS_LIST_FRAGMENT_TAG)
                .commit()
        }
    }

    private fun replaceDetailFragment(contact: Contact) {
        removeDetailFragment()
        addDetailFragment(contact.id, R.id.details_fragment)
    }

    private fun removeDetailFragment() {
        manager.findFragmentByTag(CONTACT_DETAILS_FRAGMENT_TAG)?.let { existFragment ->
            manager.beginTransaction()
                .remove(existFragment)
                .commit()
        }
    }

    private fun addDetailFragment(contactId: Int, containerViewId: Int) {
        ContactDetailsFragment.newInstance(contactId).apply {
            manager.beginTransaction().apply {
                if (!isTablet) this.addToBackStack(null)
            }
                .replace(
                    containerViewId,
                    this,
                    CONTACT_DETAILS_FRAGMENT_TAG)
                .commit()
        }
    }

    private fun addEditFragment(contact: Contact) {
        ContactEditFragment.newInstance(contact).show(manager, CONTACT_EDIT_FRAGMENT_TAG)
    }

    private fun removeEditFragment() {
        manager.findFragmentByTag(CONTACT_EDIT_FRAGMENT_TAG)?.let {
            Log.e("YES", "$it")
            manager.beginTransaction()
                .remove(it)
                .commit()
        }
    }
}