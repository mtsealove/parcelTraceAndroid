package kr.geekble.parceltracker

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Callback
import kr.geekble.parceltracker.Restful.Rest
import kr.geekble.parceltracker.Restful.TrackingDetail
import kr.geekble.parceltracker.Restful.TrackingModel
import org.jsoup.Jsoup
import retrofit2.Call
import retrofit2.Response
import java.lang.Exception
import java.text.SimpleDateFormat
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private val rest = Rest().getService()
    private lateinit var searchEt: EditText
    private lateinit var apiCb: CheckBox
    private lateinit var nameTv: TextView
    private lateinit var trackingRv: RecyclerView
    private lateinit var searchBtn: Button
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        searchEt = findViewById(R.id.mainSearchEt)
        apiCb = findViewById(R.id.apiCb)
        nameTv = findViewById(R.id.nameTv)
        trackingRv = findViewById(R.id.trackingRv)
        searchBtn = findViewById(R.id.searchBtn)

        searchBtn.setOnClickListener {
            val invoice = searchEt.text.toString()
            if (apiCb.isChecked) {
                getItemBySmartTrace(invoice)
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    getItemByHtml(invoice)
                }
            }
        }
    }


    private fun getItemBySmartTrace(invoice: String) {
        val call = rest.getTrackingInfo(
            key = "vaxLuih3CPk8MxzhZ0cdqA",
            code = "04",
            invoice = invoice
        )
        call.enqueue(object : Callback<TrackingModel> {
            override fun onResponse(call: Call<TrackingModel>, response: Response<TrackingModel>) {
                if (response.isSuccessful) {
                    response.body()?.let { res ->
                        Log.e("result", res.toString())
                        nameTv.text = res.itemName
                        val adapter = TracingAdapter(res.trackingDetails)
                        trackingRv.adapter = adapter
                    }
                } else {
                    Log.e("result", response.toString())
                }
            }
            override fun onFailure(call: Call<TrackingModel>, t: Throwable) {
                Log.e("error", t.toString())
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getItemByHtml(invoice: String) {
        try {
            val thread = Thread {
                val doc =
                    Jsoup.connect("http://nplus.doortodoor.co.kr/web/detail.jsp?slipno=${invoice}")
                        .get()
                Log.e("title", doc.title())
                doc.selectXpath("/html/body/center/table[7]/tbody")?.first()?.let { table ->
                    val trs = table.selectXpath("tr")
                    val list = ArrayList<TrackingDetail>()
                    trs.forEachIndexed() { index, tr ->
                        if (index != 0) {
                            val tds = tr.selectXpath("td")
                            val date = tds[0].text()
                            val time = tds[1].text()
                            val state = tds[3].text()
                            val content = tds[2].selectXpath("table/tbody/tr/td")
                            val location = content[0].text()
                            val telNo = content[1].text()
                            val timeLong = dateFormat.parse("${date} ${time}").time
                            val data: TrackingDetail = TrackingDetail(
                                timeLong,
                                "${date} ${time}",
                                null,
                                location,
                                state,
                                telNo,
                                "",
                                null,
                                2,
                                "",
                                ""
                            )
                            list.add(data)
                            Log.e("parse", "${date} ${time} ${state} ${location}, ${telNo}")
                        }
                    }
                    list.reverse()
                    runOnUiThread {
                        val adapter = TracingAdapter(list)
                        trackingRv.adapter = adapter
                    }
                }
            }
            thread.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}