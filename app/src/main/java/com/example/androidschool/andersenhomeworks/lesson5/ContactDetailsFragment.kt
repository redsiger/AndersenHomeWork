package com.example.androidschool.andersenhomeworks.lesson5

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.example.androidschool.andersenhomeworks.R
import com.example.androidschool.andersenhomeworks.databinding.FragmentContactDetailsBinding

class ContactDetailsFragment: Fragment(R.layout.fragment_contact_details), RepositoryListener {

    companion object {
        const val CONTACT_DETAILS_FRAGMENT_TAG = "CONTACT_DETAILS_FRAGMENT_TAG"
        private const val CONTACT_ID = "CONTACT_ID"

        fun newInstance(contactId: Int): ContactDetailsFragment {
            val args = Bundle()
            args.putInt(CONTACT_ID, contactId)

            val fragment = ContactDetailsFragment().apply {
                arguments = args
            }
            return fragment
        }
    }

    private var _viewBinding: FragmentContactDetailsBinding? = null
    private val viewBinding get() = _viewBinding!!
    private val contactId: Int by lazy {
        arguments?.getInt(CONTACT_ID) ?: -1
    }

    private val listener by lazy { requireActivity() as FragmentListener }

    override fun repositoryUpdated() {
        renderContact(listener.getContact(contactId))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _viewBinding = FragmentContactDetailsBinding.bind(view)

        listener.addRepositoryListener(this)
        renderContact(listener.getContact(contactId))
        initEditBtn()
    }

    private fun renderContact(contact: Contact) {
        with(viewBinding) {
            contactDetailsFirstName.text = contact.firstName
            contactDetailsSecondName.text = contact.secondName
            contactDetailsPhoneNumber.text = contact.phoneNumber
        }
    }

    private fun initEditBtn() {
        viewBinding.contactDetailsEditBtn.setOnClickListener {
            listener.onItemEdit(contactId)
        }
    }
}
