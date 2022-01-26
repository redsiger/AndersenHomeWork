package com.example.androidschool.andersenhomeworks.lesson6.fragments

import android.view.*
import android.widget.Filter
import android.widget.Filterable
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import com.example.androidschool.andersenhomeworks.R
import com.example.androidschool.andersenhomeworks.databinding.ItemContactRvBinding
import com.example.androidschool.andersenhomeworks.lesson6.Contact
import com.example.androidschool.andersenhomeworks.lesson6.ContactsListener

class ContactAdapter(
    private val contactsListener: ContactsListener
): RecyclerView.Adapter<ContactViewHolder>(),
    View.OnClickListener,
    View.OnLongClickListener,
    Filterable
{

    override fun onLongClick(view: View): Boolean {
        val contactId = view.tag as Int
        contactsListener.onItemClick(contactId)
        return true
    }

    override fun onClick(view: View) {
        val contactId = view.tag as Int
        when (view.id) {
            R.id.item_contact_btn_more -> {showPopupMenu(view)}
            else -> contactsListener.onItemClick(contactId)
        }
    }

    private var _contactList: List<Contact> = emptyList()

    fun setList(contactList: List<Contact>) {
        val callback = ContactDiffUtil(_contactList, contactList)
        val diffResult = DiffUtil.calculateDiff(callback)
        _contactList = contactList
        diffResult.dispatchUpdatesTo(this)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemContactRvBinding.inflate(inflater, parent, false)

        binding.root.setOnClickListener(this)
        binding.itemContactBtnMore.setOnClickListener(this)
        binding.root.setOnLongClickListener(this)
        return ContactViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = _contactList[position]
        holder.bind(contact)
        holder.itemView.tag = contact.id
        holder.viewBinding.itemContactBtnMore.tag = contact.id
    }

    override fun getItemCount(): Int = _contactList.size

    private fun showPopupMenu(view: View) {
        val contactId = view.tag as Int
        PopupMenu(view.context, view, Gravity.END, R.attr.actionOverflowMenuStyle, 0).apply {
            inflate(R.menu.contact_menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.contact_menu_item_delete -> {
                        contactsListener.onItemDelete(contactId)
                        true
                    }
                    else -> {true}
                }
            }
        }.show()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            private val filterResults = FilterResults()
            override fun performFiltering(query: CharSequence?): FilterResults {
                TODO("Not yet implemented")
            }

            override fun publishResults(query: CharSequence?, p1: FilterResults?) {
                TODO("Not yet implemented")
            }

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
                .placeholder(R.drawable.ic_baseline_person_24)
                .signature(ObjectKey(contact.imgUrl + contact.id))
                .into(itemContactImage)
        }
    }

}

class ContactDiffUtil(
    private val oldList: List<Contact>,
    private val newList: List<com.example.androidschool.andersenhomeworks.lesson6.Contact>
): DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem.firstName == newItem.firstName
    }
}
