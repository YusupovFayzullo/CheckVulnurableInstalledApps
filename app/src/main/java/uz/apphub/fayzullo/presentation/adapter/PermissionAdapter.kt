package uz.apphub.fayzullo.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.apphub.fayzullo.databinding.ItemPermissionBinding
import uz.apphub.fayzullo.domain.model.PermissionModel

class PermissionAdapter :
    RecyclerView.Adapter<PermissionAdapter.PermissionViewHolder>() {

    private val permissionList: MutableList<PermissionModel> = ArrayList()

    inner class PermissionViewHolder(private val binding: ItemPermissionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(permission: PermissionModel) {
            binding.permission.text = permission.name
            binding.permission.isChecked = permission.isGranted
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PermissionViewHolder {
        return PermissionViewHolder(
            ItemPermissionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: PermissionViewHolder, position: Int) {
        holder.bind(permissionList[position])
    }

    override fun getItemCount(): Int {
        return permissionList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun initList(permissions: List<PermissionModel>) {
        permissionList.clear()
        permissionList.addAll(permissions)
        notifyDataSetChanged()
    }
}
