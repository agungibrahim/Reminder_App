package com.example.reminder_app.pages.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.reminder_app.R
import com.example.reminder_app.network.response.ResponseReminder
import kotlinx.android.synthetic.main.item_recyclerview_home_layaout.view.*

class AdapterHome(private val context: Context, val page: HomeActivity) : RecyclerView.Adapter<AdapterHome.ItemHolder>() {

    private var listReminder: ArrayList<ResponseReminder.Todos> = ArrayList()

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            LayoutInflater.from(viewGroup.context).inflate(
                R.layout.item_recyclerview_home_layaout,
                viewGroup,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return listReminder.size
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val item = listReminder[position]

        holder.titleText.text = item.title
        holder.descText.text = item.description
        holder.dateText.text = item.dateTime
        holder.itemLayout.setOnClickListener {
            page.sendNotification(context, item.title.toString(), item.description.toString(), item.dateTime.toString())
        }
    }

    inner class ItemHolder(v: View) : RecyclerView.ViewHolder(v) {
        var titleText = v.title_text
        var descText = v.desc_text
        var dateText = v.date_time_text
        var itemLayout = v.item_layout
    }

    fun updateList(listItem : List<ResponseReminder.Todos>) {
        listReminder.clear()
        listReminder.addAll(listItem)
        notifyDataSetChanged()
    }
}