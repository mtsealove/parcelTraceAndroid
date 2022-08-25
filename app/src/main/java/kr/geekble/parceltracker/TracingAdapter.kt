package kr.geekble.parceltracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.geekble.parceltracker.Restful.TrackingDetail

class TracingAdapter(private val list: List<TrackingDetail>) :
    RecyclerView.Adapter<TracingAdapter.ViewHolder>() {

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val dateTv: TextView = v.findViewById(R.id.dateTv)
        private val stateTv: TextView = v.findViewById(R.id.stateTv)
        private val locationTv: TextView = v.findViewById(R.id.locationTv)

        fun bind(detail: TrackingDetail) {
            dateTv.text = detail.timeString
            stateTv.text = detail.kind
            locationTv.text = detail.where
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_track, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}