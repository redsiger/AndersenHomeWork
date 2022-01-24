package com.example.androidschool.andersenhomeworks.lesson5.fragments

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.example.androidschool.andersenhomeworks.R
import com.example.androidschool.andersenhomeworks.databinding.FragmentContactsListBinding
import com.example.androidschool.andersenhomeworks.lesson5.Contact
import com.example.androidschool.andersenhomeworks.lesson5.FragmentListener
import com.example.androidschool.andersenhomeworks.lesson5.RepositoryListener

class ContactsListFragment: Fragment(R.layout.fragment_contacts_list), RepositoryListener {

    companion object {
        const val TAG = "CONTACTS_LIST_FRAGMENT_TAG"

        fun newInstance(): ContactsListFragment = ContactsListFragment()
    }

    private var _viewBinding: FragmentContactsListBinding? = null
    private val viewBinding get() = _viewBinding!!
    private val listener by lazy { requireActivity() as FragmentListener }
    private val contactList get() = listener.getContacts()

    override fun repositoryUpdated() = render(contactList)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _viewBinding = FragmentContactsListBinding.bind(view)

        listener.addRepositoryListener(this)
        render(contactList)
    }

    private fun render(contactList: List<Contact>) {
        viewBinding.contactsList.removeAllViews()
        initList(contactList)
    }

    /**
     * Populates ViewGroup by children in form of contact
     */
    private fun initList(contactList: List<Contact>) {
        contactList.forEach { contact ->
            val linearLayout = viewBinding.contactsList
            val view = layoutInflater.inflate(
                R.layout.item_contact,
                linearLayout,
                false
            ) as ViewGroup
            fillViewByContact(view, contact)
            linearLayout.addView(view)
            view.setOnClickListener {
                listener.onItemClick(contact.id)
            }
        }
    }

    /**
     * Fills ViewGroup by Contact's data
     */
    private fun fillViewByContact(view: ViewGroup, contact: Contact) {
        (view.getChildAt(0) as ViewGroup).children.forEach { view ->
            when {
                view.id == R.id.item_contact_firstName -> (view as TextView).text = contact.firstName
                view.id == R.id.item_contact_secondName -> (view as TextView).text = contact.lastName
                view.id == R.id.item_contact_phoneNumber -> (view as TextView).text = contact.phoneNumber
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        listener.removeRepositoryListener(this)
    }
}