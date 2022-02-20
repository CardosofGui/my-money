package com.example.mymoney.presenter.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.mymoney.R
import com.example.mymoney.databinding.CardviewStatementBinding
import com.example.mymoney.domain.Economy
import com.example.mymoney.domain.Statement
import com.example.mymoney.presenter.EconomyView.Companion.formatMoney

class StatementAdapter(
    var statementList: ArrayList<Statement>,
    var itemInterface: ItemInterface
) : RecyclerView.Adapter<RecyclerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        return RecyclerViewHolder(CardviewStatementBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = statementList.size

    @SuppressLint("SetTextI18n", "ResourceAsColor")
    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val statement = statementList[position]
        val binding = holder.binding

        binding.txtNameStatement.text = statement.statementName
        binding.txtDateStatement.text = statement.statementDate
        binding.txtQuantityMoney.text = "R$ ${formatMoney(statement.statementMoney)}"

        val colorType = if(statement.statementType == "Saida") "#720000" else "#097200"
        val cardColor = if(statement.statementType == "Saida") "#EAC1C1" else "#D6EAC1"

        binding.txtNameStatement.setTextColor(Color.parseColor(colorType))
        binding.txtDateStatement.setTextColor(Color.parseColor(colorType))
        binding.txtQuantityMoney.setTextColor(Color.parseColor(colorType))
        binding.root.setCardBackgroundColor(Color.parseColor(cardColor))

        binding.root.setOnLongClickListener {
            val popupMenu = PopupMenu(it.context, it)
            popupMenu.menuInflater.inflate(R.menu.menu_item, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.item_edit -> {
                        itemInterface.editItem(null, statement)
                    }
                    R.id.item_delete -> {
                        itemInterface.deleteItem(null, statement.id)
                    }
                }

                return@setOnMenuItemClickListener false
            }

            popupMenu.show()
            return@setOnLongClickListener true
        }
    }

    fun submitList(newList: ArrayList<Statement>){
        statementList = newList
        notifyDataSetChanged()
    }
}

class RecyclerViewHolder(var binding : CardviewStatementBinding) : RecyclerView.ViewHolder(binding.root)
