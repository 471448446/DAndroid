package better.hellokotlin.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import better.hellokotlin.R
import com.antonioleiva.weatherapp.data.server.Forecast
import com.antonioleiva.weatherapp.data.server.ForecastResult
import com.antonioleiva.weatherapp.extensions.toDateString
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_list_main.view.*

/**
 * Created by better on 2017/7/28 15:23.
 */
class ForecastListAdapter(var item: ForecastResult? = null) : RecyclerView.Adapter<ForecastListAdapter.Holder>() {

    //    val listener: (Forecast) -> Unit = { bean -> }
    var listener: (Forecast) -> Unit = { }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): Holder =
            Holder(LayoutInflater.from(parent?.context).inflate(R.layout.item_list_main, parent, false))

    //        override fun getItemCount(): Int = if (null == item) 0 else item.list.size
    override fun getItemCount(): Int = item?.list?.size ?: 0

    fun setListData(item: ForecastResult) {
        this.item = item
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: Holder?, position: Int) {

        val bean: Forecast = item!!.list[position]
        //可以方便使用this
        with(bean) {
            holder?.dateTv?.text = dt.toDateString()
            holder?.descriptionTv?.text = weather[0].description
            holder?.maxTemperatureTv?.text = "${temp.max}ºC"
            holder?.itemView?.minTemperature?.text = "${temp.min}ºC"
            Glide.with(holder?.icon?.context)
                    .load("http://openweathermap.org/img/w/${weather[0].icon}.png")
                    .into(holder?.icon)
        }
        holder?.itemView?.setOnClickListener({ listener(bean) })

//        holder?.dateTv?.text = bean.dt.toDateString()
//        holder?.descriptionTv?.text = bean.weather[0].description
//        holder?.maxTemperatureTv?.text = "${bean.temp.max}ºC"
//        holder?.minTemperatureTv?.text = "${bean.temp.min}ºC"
//        Glide.with(holder?.icon?.context)
//                .load("http://openweathermap.org/img/w/${bean.weather[0].icon}.png")
//                .into(holder?.icon)
    }

    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        /*
         * var textView: TextView
         * init {
         * textView = view.findViewById(R.city_id.item_txt) as TextView
         * }
         * 提示：can be joined with assignment
         * 就是说不需要在init代码块中初始化，可以直接在表达式中赋值
         */

        var icon: ImageView = view.findViewById(R.id.icon) as ImageView
        var dateTv: TextView = view.findViewById(R.id.date) as TextView
        var descriptionTv: TextView = view.findViewById(R.id.description) as TextView
        var maxTemperatureTv: TextView = view.findViewById(R.id.maxTemperature) as TextView
//        var minTemperatureTv: TextView = view.findViewById(R.city_id.minTemperature) as TextView

    }

}