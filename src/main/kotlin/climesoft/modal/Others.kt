package climesoft.modal

import com.beust.klaxon.Json

data class Others (
    val status: String,
    val currency: String,
    val website: String,

    @Json(name = "currency_symbol")
    val currencySymbol: String,

    @Json(name = "order_total")
    val orderTotal: String,

    @Json(name = "payment_method")
    val paymentMethod: String,

    @Json(name = "payment_method_title")
    val paymentMethodTitle: String,

    @Json(name = "order_date_created")
    val orderDateCreated: String,

    @Json(name = "shipping_method")
    val shippingMethod: String,

    @Json(name = "shipping_total")
    val shippingTotal: String,

    @Json(name = "shipping_tax")
    val shippingTax: String,

    @Json(name = "shipping_country")
    val shippingCountry: String,

    @Json(name = "billing_country")
    val billingCountry: String,

    val dirSuffix: String,

    val note: String
)