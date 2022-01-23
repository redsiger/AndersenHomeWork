package com.example.androidschool.andersenhomeworks.lesson5

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.androidschool.andersenhomeworks.databinding.FragmentContactEditBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ContactEditFragment: BottomSheetDialogFragment() {

    companion object {
        const val CONTACT_EDIT_FRAGMENT_TAG = "CONTACT_EDIT_FRAGMENT_TAG"
        private const val CONTACT = "CONTACT"

        fun newInstance(contact: Contact): ContactEditFragment {
            val args = Bundle()
            args.putParcelable(CONTACT, contact)

            val fragment = ContactEditFragment().apply {
                arguments = args
            }
            return fragment
        }
    }

    private var _viewBinding: FragmentContactEditBinding? = null
    private val viewBinding get() = _viewBinding!!
    private val currentContact: Contact by lazy {
        arguments?.getParcelable(CONTACT) ?: Contact()
    }
    private val listener by lazy {  requireActivity() as FragmentListener  }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding = FragmentContactEditBinding.inflate(inflater, container, false)

        render(currentContact)

        with(viewBinding) {
            contactEditSaveBtn.setOnClickListener {
                val contact = Contact(
                    currentContact.id,
                    this.contactEditFirstName.editText!!.text.toString(),
                    this.contactEditSecondName.editText!!.text.toString(),
                    this.contactEditPhoneNumber.editText!!.text.toString()
                )
                listener.onItemSave(contact)
            }
        }


        return viewBinding.root
    }

    private fun render(contact: Contact) {
        with(viewBinding) {
            contactEditFirstName.editText!!.text = toEditable(contact.firstName)
            contactEditSecondName.editText!!.text = toEditable(contact.secondName)
            contactEditPhoneNumber.editText!!.text = toEditable(contact.phoneNumber)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e(CONTACT_EDIT_FRAGMENT_TAG, "onDestroy")
    }
}

fun ContactEditFragment.toEditable(input: String): Editable {
    return Editable.Factory.getInstance().newEditable(input)
}