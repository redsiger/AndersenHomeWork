package com.example.androidschool.andersenhomeworks.lesson5.fragments

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.androidschool.andersenhomeworks.databinding.FragmentContactEditBinding
import com.example.androidschool.andersenhomeworks.lesson5.Contact
import com.example.androidschool.andersenhomeworks.lesson5.FragmentListener
import com.example.androidschool.andersenhomeworks.lesson5.RepositoryListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ContactEditFragment: BottomSheetDialogFragment(), RepositoryListener {

    companion object {
        const val TAG = "CONTACT_EDIT_FRAGMENT_TAG"

        fun newInstance(): ContactEditFragment {
            return ContactEditFragment()
        }
    }

    private var _viewBinding: FragmentContactEditBinding? = null
    private val viewBinding get() = _viewBinding!!

    private val listener by lazy { requireActivity() as FragmentListener }
    private val contactId: Int by lazy { listener.getCurrentId() }
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
                        contact.id,
                        this.contactEditFirstName.editText!!.text.toString(),
                        this.contactEditSecondName.editText!!.text.toString(),
                        this.contactEditPhoneNumber.editText!!.text.toString()
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