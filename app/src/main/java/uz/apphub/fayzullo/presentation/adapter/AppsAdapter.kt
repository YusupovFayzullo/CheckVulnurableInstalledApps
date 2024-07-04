package uz.apphub.fayzullo.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.apphub.fayzullo.databinding.ItemAppsBinding
import uz.apphub.fayzullo.domain.model.AppsModel
import uz.apphub.fayzullo.utils.toDrawable

class AppsAdapter() :
    RecyclerView.Adapter<AppsAdapter.AppsViewHolder>() {
    private var appsList = mutableListOf<AppsModel>()
    private var listener: AppClickListener? = null

    inner class AppsViewHolder(private val binding: ItemAppsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(app: AppsModel){
            binding.appIcon.setImageDrawable(app.appIcon.toDrawable(binding.appItem.context))
            binding.appName.text = app.appName

            binding.appItem.setOnClickListener {
                listener?.onItemClick(app)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppsViewHolder {
        return AppsViewHolder(
            ItemAppsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: AppsViewHolder, position: Int) {
        holder.bind(appsList[position])
    }

    override fun getItemCount(): Int {
        return appsList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun initList(list: List<AppsModel>) {
        appsList.clear()
        appsList.addAll(list)
        notifyDataSetChanged()
    }

    fun initListener(listener: AppClickListener) {
        this.listener = listener
    }

    interface AppClickListener {
        fun onItemClick(app: AppsModel)
    }
}





