package kr.geekble.parceltracker.Restful

data class TrackingDetail(
    val time: Long,
    val timeString: String,
    val code: String?,
    val where: String,
    val kind: String,
    val telno: String,
    val telno2: String,
    val remark: String?,
    val level: Int,
    val manName: String,
    val manPic: String
)

data class TrackingModel(
    val result: String,
    val senderName: String,
    val receiverName: String,
    val itemName: String,
    val invoiceNo: String,
    val receiverAddr: String,
    val orderNumber: Int?,
    val adUrl: String,
    val estimate: String?,
    val level: Int,
    val complete: Boolean,
    val recipient: String,
    val itemImage: String,
    val trackingDetails: List<TrackingDetail>
)
