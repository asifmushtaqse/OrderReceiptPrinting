package climesoft.modal

import com.beust.klaxon.Json

data class Upload(
    val folder: String? = null,
    val image: String? = null,
    val quantity: String,
    @Json(name = "variation_id")
    val variationId: String,
    @Json(name = "variation_label")
    val variationLabel: String = "",
)