package com.example.androidschool.andersenhomeworks.lesson6.fragments

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.androidschool.andersenhomeworks.R
import com.example.androidschool.andersenhomeworks.lesson6.ContactsDataSource.Companion.DEFAULT_CONTACT_ID

class DeleteContactDialogFragment: DialogFragment() {

    companion object {
        const val TAG = "DELETE_DIALOG_FRAGMENT_TAG"
        private const val CONTACT_ID = "CONTACT_ID"

        fun newInstance(contactId: Int): DeleteContactDialogFragment {
            val args = Bundle()
            args.putInt(CONTACT_ID, contactId)

            val fragment = DeleteContactDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private val listener by lazy { requireActivity() as DialogListener }
    private val contactId by lazy { arguments?.getInt(CONTACT_ID) ?: DEFAULT_CONTACT_ID }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.alert_dialog_delete_title)
            .setMessage(R.string.alert_dialog_delete_message)
            .setPositiveButton(R.string.alert_dialog_delete_accept) { _, _ ->
                listener.accept(contactId)
            }
            .setNegativeButton(R.string.alert_dialog_delete_reject) { dialog, _ ->
                dialog.cancel()
            }.create()
    }
}