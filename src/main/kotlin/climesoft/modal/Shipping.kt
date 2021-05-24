package climesoft.modal

import com.beust.klaxon.Json

data class Shipping (
    @Json(name = "first_name")
    val firstName: String,

    @Json(name = "last_name")
    val lastName: String,

    val company: String,

    @Json(name = "address_1")
    val address1: String,

    @Json(name = "address_2")
    val address2: String,

    val city: String,
    val state: String,
    val postcode: String,
    val country: String
){
    override fun toString(): String {
        var alternativeAddress = "";
        if(this.address2.isNotEmpty()){
            alternativeAddress= "$address2\n";
        }
        return  "$firstName $lastName \n" +
                "$address1\n" +
                alternativeAddress +
                "$city - $postcode\n" +
                "$state, $country"
    }
}