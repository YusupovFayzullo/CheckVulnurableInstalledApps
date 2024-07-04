package uz.apphub.fayzullo.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.apphub.fayzullo.R
import uz.apphub.fayzullo.databinding.SignatureItemBinding
import uz.apphub.fayzullo.domain.model.ScannerModel
import uz.apphub.fayzullo.utils.toFormattedDateString

class ScannerAdapter :
    RecyclerView.Adapter<ScannerAdapter.ScannerViewHolder>() {

    private val permissionList: MutableList<ScannerModel> = ArrayList()
    inner class ScannerViewHolder(private val binding: SignatureItemBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(signature: ScannerModel,position: Int) {
            binding.id.text = "${position + 1}.  "
            binding.date.text = signature.created.toFormattedDateString()
             if (signature.isSecured){
                 binding.secured.text = "Xavf aniqlanmadi "
                 binding.secured.setTextColor(binding.secured.context.getColor(R.color.green))
                 binding.id.setTextColor(binding.secured.context.getColor(R.color.green))
            } else {
                 binding.secured.text = "Xavf aniqlandi "
                 binding.id.setTextColor(binding.secured.context.getColor(R.color.red))
                 binding.secured.setTextColor(binding.secured.context.getColor(R.color.red))
             }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScannerViewHolder {
        return ScannerViewHolder(
            SignatureItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ScannerViewHolder, position: Int) {
        holder.bind(permissionList[position],position)
    }

    override fun getItemCount(): Int {
        return permissionList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(permissions: List<ScannerModel>) {
        permissionList.clear()
        permissionList.addAll(permissions)
        notifyDataSetChanged()
    }
}
