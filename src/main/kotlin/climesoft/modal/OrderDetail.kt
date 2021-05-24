package climesoft.modal

data class OrderDetail(
    val id : String,
    val product: ArrayList<Product>,
    val billing: Billing,
    val shipping: Shipping,
    val others: Others
)