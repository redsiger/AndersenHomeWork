package com.example.androidschool.andersenhomeworks.lesson6.fragments

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import com.example.androidschool.andersenhomeworks.R
import com.example.androidschool.andersenhomeworks.databinding.ItemContactRvBinding
import com.example.androidschool.andersenhomeworks.lesson5.Contact

class ContactAdapter(
    private val actionClick: (id: Int) -> Unit,
    private val actionLongClick: (id: Int) -> Unit
): RecyclerView.Adapter<ContactViewHolder>() {

    private var _contactList: MutableList<Contact> = mutableListOf()
    fun setList(contactList: List<Contact>) {
        val inputList = contactList.toMutableList()
        val callback = ContactDiffUtil(_contactList, inputList)
        val diffResult = DiffUtil.calculateDiff(callback)

        _contactList = inputList
//        notifyDataSetChanged()
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_contact_rv, parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(_contactList[position])
        val contactId = _contactList[position].id
        holder.itemView.setOnClickListener {
            actionClick(position)
        }
        holder.itemView.setOnLongClickListener {
            actionLongClick(position)
            true
        }
        (holder.viewBinding.itemContactImage.setOnClickListener {
            PopupMenu(it.context, it).apply {
                inflate(R.menu.contact_menu)
                setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.contact_menu_item_delete -> {
                            actionLongClick(position)
                            true
                        }
                        else -> {true}
                    }
                }
            }.show()
        })
    }

    override fun getItemCount(): Int = _contactList.size

    class ContactDiffUtil(
        private val oldList: List<Contact>,
        private val newList: List<Contact>
    ): DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = oldList[newItemPosition]
            return oldItem.firstName == newItem.firstName
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = oldList[newItemPosition]
            return oldItem.firstName == newItem.firstName
        }
    }
}

class ContactViewHolder(view: View): RecyclerView.ViewHolder(view) {

    val viewBinding = ItemContactRvBinding.bind(view)

    fun bind(contact: Contact) {
        with(viewBinding) {
            itemContactFirstName.text = contact.firstName
            itemContactLastName.text = contact.lastName
            itemContactPhoneNumber.text = contact.phoneNumber
            Glide.with(itemView.context)
                .load(contact.imgUrl)
                .signature(ObjectKey(contact.imgUrl + contact.id))
                .into(itemContactImage)
        }
    }
}