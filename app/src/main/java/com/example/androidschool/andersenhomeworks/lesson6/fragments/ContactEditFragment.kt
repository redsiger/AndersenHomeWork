package com.example.androidschool.andersenhomeworks.lesson6.fragments

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.androidschool.andersenhomeworks.databinding.FragmentContactEditBinding
import com.example.androidschool.andersenhomeworks.lesson6.Contact
import com.example.androidschool.andersenhomeworks.lesson6.ContactsDataSource.Companion.DEFAULT_CONTACT_ID
import com.example.androidschool.andersenhomeworks.lesson6.ContactsListener
import com.example.androidschool.andersenhomeworks.lesson6.RepositoryListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ContactEditFragment: BottomSheetDialogFragment(), RepositoryListener {

    companion object {
        const val TAG = "CONTACT_EDIT_FRAGMENT_TAG"
        private const val CONTACT_ID = "CONTACT_ID"

        fun newInstance(contactId: Int): ContactEditFragment {
            val args = Bundle()
            args.putInt(CONTACT_ID, contactId)

            val fragment = ContactEditFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private var _viewBinding: FragmentContactEditBinding? = null
    private val viewBinding get() = _viewBinding!!

    private val listener by lazy { requireActivity() as ContactsListener }
    private val contactId: Int by lazy { arguments?.getInt(CONTACT_ID) ?: DEFAULT_CONTACT_ID }
    private val contact: Contact get() =  listener.getContact(contactId)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding = FragmentContactEditBinding.inflate(inflater, container, false)

        render(contact)

        with(viewBinding) {
            contactEditSaveBtn.setOnClickListener {
                listener.onItemSave(
                    contact.copy(
                        firstName =  this.contactEditFirstName.editText!!.text.toString(),
                        lastName =  this.contactEditSecondName.editText!!.text.toString(),
                        phoneNumber =  this.contactEditPhoneNumber.editText!!.text.toString()
                    )
                )
            }
            contactEditCancelBtn.setOnClickListener {
                listener.onItemCancel()
            }
        }

        return viewBinding.root
    }

    private fun render(contact: Contact) {
        with(viewBinding) {
            contactEditFirstName.editText!!.text = toEditable(contact.firstName)
            contactEditSecondName.editText!!.text = toEditable(contact.lastName)
            contactEditPhoneNumber.editText!!.text = toEditable(contact.phoneNumber)
        }
    }

    override fun repositoryUpdated() {
        render(contact)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        listener.removeRepositoryListener(this)
    }
}

fun ContactEditFragment.toEditable(input: String): Editable {
    return Editable.Factory.getInstance().newEditable(input)
}