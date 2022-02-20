package com.example.mymoney.presenter.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.mymoney.R
import com.example.mymoney.databinding.CardviewEconomyBinding
import com.example.mymoney.domain.Economy
import com.example.mymoney.presenter.EconomyView.Companion.formatMoney

class EconomyAdapter(
    var listEconomy: ArrayList<Economy>,
    var economyInterface: EconomyInterface
) : RecyclerView.Adapter<EconomyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EconomyViewHolder {
        return EconomyViewHolder(CardviewEconomyBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = listEconomy.size

    override fun onBindViewHolder(holder: EconomyViewHolder, position: Int) {
        val economy = listEconomy[position]
        val binding = holder.binding

        val colorType = if(economy.actualEconomy < 0) "#720000" else "#097200"


        binding.root.setOnClickListener {
            economyInterface.actionChangeActivity(economy.id ?: -1, economy.economyName, economy.economyGoal)
        }

        binding.labelMeta.setTextColor(Color.parseColor(colorType))
        binding.txtNameEconomy.text = economy.economyName
        binding.labelMeta.text = "R$ " + formatMoney(economy.actualEconomy) + " /"
        binding.labelSaldo.text = " Meta \n " + "R$ " + formatMoney(economy.economyGoal)

        binding.root.setOnLongClickListener {

            val popupMenu = PopupMenu(it.context, it)
            popupMenu.menuInflater.inflate(R.menu.menu_item, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.item_edit -> {
                        economyInterface.editItem(economy, null)
                    }
                    R.id.item_delete -> {
                        economyInterface.deleteItem(economy.id, null)
                    }
                }

                return@setOnMenuItemClickListener false
            }

            popupMenu.show()
            return@setOnLongClickListener true
        }


    }

    fun submitList(newList : ArrayList<Economy>){
        listEconomy = newList
        notifyDataSetChanged()
    }
}

class EconomyViewHolder(val binding : CardviewEconomyBinding) : RecyclerView.ViewHolder(binding.root)
