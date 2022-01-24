package com.example.androidschool.andersenhomeworks.lesson5

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.androidschool.andersenhomeworks.R
import com.example.androidschool.andersenhomeworks.databinding.ActivityLesson5Binding
import com.example.androidschool.andersenhomeworks.databinding.ActivityLesson5LandBinding
import com.example.androidschool.andersenhomeworks.lesson5.fragments.ContactDetailsFragment
import com.example.androidschool.andersenhomeworks.lesson5.fragments.ContactEditFragment
import com.example.androidschool.andersenhomeworks.lesson5.fragments.ContactsListFragment
import com.github.javafaker.Faker

class ActivityLesson5: AppCompatActivity(), FragmentListener {

    companion object {
        private const val CONTACT_LIST = "CONTACT_LIST"
        private const val NO_CONTACTS = -1
    }

    private val isTablet: Boolean by lazy { this.resources.getBoolean(R.bool.isTablet) }
    private var _tabletBinding: ActivityLesson5LandBinding? = null
    private val tabletBinding get() = _tabletBinding!!

    private var _phoneBinding: ActivityLesson5Binding? = null
    private val phoneBinding get() = _phoneBinding!!

    private val manager: FragmentManager by lazy { supportFragmentManager }

    private val faker = Faker()

    private var _contactList = mutableListOf<Contact>()
    private val contactList get() = _contactList
    private var currentContactId = 0
    private val repositoryListeners = mutableListOf<RepositoryListener>()

    override fun getCurrentId(): Int {
        return if (contactList.size > 0) currentContactId else NO_CONTACTS
    }
    override fun getContact(id: Int): Contact {
        return if(contactList.size > 0) contactList[id] else Contact()
    }
    override fun getContacts(): List<Contact> = contactList

    override fun addRepositoryListener(listener: RepositoryListener) {
        repositoryListeners.add(listener)
    }

    override fun removeRepositoryListener(listener: RepositoryListener) {
        repositoryListeners.remove(listener)
    }

    private fun updateListeners() = repositoryListeners.forEach { it.repositoryUpdated() }

    override fun onItemClick(id: Int) {
        when {
            isTablet -> {
                if (currentContactId != id) replaceDetailFragment()
                currentContactId = id
            }
            else -> {
                currentContactId = id
                replaceDetailFragment()
            }
        }
    }

    override fun onItemEdit(id: Int) {
        addFragment(0, ContactEditFragment.TAG)
    }

    override fun onItemSave(contact: Contact) {
        if (contact.id == NO_CONTACTS) return
        contactList.removeAt(contact.id)
        contactList.add(contact.id, contact)
        removeFragment(ContactEditFragment.TAG)
        updateListeners()
    }

    override fun onItemCancel() {
        removeFragment(ContactEditFragment.TAG)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        savedInstanceState?.let {
            _contactList = it.getParcelableArrayList(CONTACT_LIST) ?: ArrayList()
        } ?: run {
            _contactList = populateContacts(3)
        }

        initView()
        when {
            isTablet -> setTabletFragments()
            else -> setPhoneFragments()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList(CONTACT_LIST, ArrayList(contactList))
        super.onSaveInstanceState(outState)
    }

    private fun initView() {
        when {
            isTablet -> initTabletView()
            else -> initPhoneView()
        }
    }

    /**
     * Initializes tablet layout
     */
    private fun initTabletView() {
        _tabletBinding = ActivityLesson5LandBinding.inflate(layoutInflater)
        setContentView(tabletBinding.root)
    }

    /**
     * Initializes phone layout
     */
    private fun initPhoneView() {
        _phoneBinding = ActivityLesson5Binding.inflate(layoutInflater)
        setContentView(phoneBinding.root)
    }

    private fun setTabletFragments() {
        replaceListFragment()
        replaceDetailFragment()
    }

    private fun setPhoneFragments() {
        replaceListFragment()
    }

    private fun replaceListFragment() {
        val tag = ContactsListFragment.TAG
        removeFragment(tag)
        addFragment(R.id.fragment_main, tag)
    }

    private fun replaceDetailFragment() {
        val tag = ContactDetailsFragment.TAG
        removeFragment(tag)
        when {
            isTablet -> { addFragment(R.id.fragment_details, tag) }
            else -> { addFragment(R.id.fragment_main, tag) }
        }

    }

    private fun addFragment(containerViewId: Int, tag: String) {
        when (tag) {
            ContactsListFragment.TAG -> replace(
                ContactsListFragment.newInstance(),
                containerViewId,
                tag
            )
            ContactDetailsFragment.TAG -> replace(
                ContactDetailsFragment.newInstance(),
                containerViewId,
                tag
            )
            ContactEditFragment.TAG -> ContactEditFragment.newInstance()
                .show(manager, ContactEditFragment.TAG)
        }
    }

    private fun replace(fragment: Fragment, containerViewId: Int, tag: String) {
        manager
            .beginTransaction()
            .apply { if (!isTablet && tag == ContactDetailsFragment.TAG) this.addToBackStack(tag) }
            .replace(
                containerViewId,
                fragment,
                tag
            )
            .commit()
    }

    private fun removeFragment(tag: String) {
        manager.findFragmentByTag(tag)?.let { existFragment ->
            manager.beginTransaction()
                .remove(existFragment)
                .commit()
        }
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
}