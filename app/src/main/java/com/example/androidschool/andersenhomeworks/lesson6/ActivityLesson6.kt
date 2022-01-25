package com.example.androidschool.andersenhomeworks.lesson6

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.androidschool.andersenhomeworks.R
import com.example.androidschool.andersenhomeworks.databinding.ActivityLesson6Binding
import com.example.androidschool.andersenhomeworks.databinding.ActivityLesson6LandBinding
import com.example.androidschool.andersenhomeworks.lesson6.fragments.ContactDetailsFragment
import com.example.androidschool.andersenhomeworks.lesson6.fragments.ContactEditFragment
import com.example.androidschool.andersenhomeworks.lesson6.fragments.ContactRecyclerListFragment
import com.example.androidschool.andersenhomeworks.lesson6.fragments.ContactsDataSource

class ActivityLesson6: AppCompatActivity(), FragmentListener {

    companion object {
        private const val CONTACT_LIST = "CONTACT_LIST"
        private const val NO_CONTACTS = -1
    }

    private val isTablet: Boolean by lazy { this.resources.getBoolean(R.bool.isTablet) }
    private var _tabletBinding: ActivityLesson6LandBinding? = null
    private val tabletBinding get() = _tabletBinding!!

    private var _phoneBinding: ActivityLesson6Binding? = null
    private val phoneBinding get() = _phoneBinding!!

    private val manager: FragmentManager by lazy { supportFragmentManager }

    private val service = ContactsDataSource.Local()

    private var currentContactId = service.getDefaultId()

    private val repositoryListeners = mutableListOf<RepositoryListener>()

    override fun getCurrentId(): Int = currentContactId
    override fun getContact(id: Int): Contact = service.getContact(id)
    override fun getContacts(): List<Contact> = service.getContacts()

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
                if (currentContactId != id) {
                    currentContactId = id
                    replaceDetailFragment()
                }
            }
            else -> {
                currentContactId = id
                replaceDetailFragment()
            }
        }
    }

    override fun onItemEdit(position: Int) {
        addFragment("", 0, ContactEditFragment.TAG)
    }

    override fun onItemSave(contact: Contact) {
        service.editContact(contact)
        removeFragment(ContactEditFragment.TAG)
        updateListeners()
    }

    override fun onItemDelete(id: Int) {
        service.deleteContact(id)
        if (currentContactId == id) currentContactId = service.getDefaultId()
        updateListeners()
    }

    override fun onItemCancel() {
        removeFragment(ContactEditFragment.TAG)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        when {
            isTablet -> setTabletFragments()
            else -> setPhoneFragments()
        }
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
        _tabletBinding = ActivityLesson6LandBinding.inflate(layoutInflater)
        setContentView(tabletBinding.root)
    }

    /**
     * Initializes phone layout
     */
    private fun initPhoneView() {
        _phoneBinding = ActivityLesson6Binding.inflate(layoutInflater)
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
        val tag = ContactRecyclerListFragment.TAG
        removeFragment(tag)
        addFragment(ContactRecyclerListFragment::class.java.name, R.id.fragment_main, tag)
    }

    private fun replaceDetailFragment() {
        val tag = ContactDetailsFragment.TAG
        removeFragment(tag)
        when {
            isTablet -> { addFragment(
                ContactDetailsFragment::class.java.name,
                R.id.fragment_details,
                tag
            )}
            else -> { addFragment(
                ContactDetailsFragment::class.java.name,
                R.id.fragment_main,
                tag
            ) }
        }

    }

    private fun addFragment(fragmentName: String, containerViewId: Int, tag: String) {
        when (tag) {
            ContactEditFragment.TAG -> ContactEditFragment.newInstance()
                .show(manager, ContactEditFragment.TAG)
            else -> replace(
                createFragment(fragmentName),
                containerViewId,
                tag
            )
        }
    }

    private fun createFragment(fragmentClassName: String): Fragment {
        return Class.forName(fragmentClassName).newInstance() as Fragment
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
}