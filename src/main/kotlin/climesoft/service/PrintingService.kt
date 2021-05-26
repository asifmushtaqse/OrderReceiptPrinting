package climesoft.service

import climesoft.modal.OrderDetail
import climesoft.util.saveTicket
import climesoft.util.ticketExists
import java.io.ByteArrayInputStream
import javax.print.*


class PrintingService {

    private val printService: PrintService? = PrintServiceLookup.lookupDefaultPrintService()

    companion object {
        val instance = PrintingService()
    }

    fun printServiceAvailable() = printService != null

     private fun printReceipt(receiptText: String, order: OrderDetail, printForcefully: Boolean = false) {
        if (printService != null) {

            val printJob = printService.createPrintJob()
            val doc: Doc = SimpleDoc( ByteArrayInputStream(receiptText.toByteArray()), DocFlavor.INPUT_STREAM.AUTOSENSE, null)
            try {
                if(!ticketExists((order.id + order.others.dirSuffix))){
                    printJob.print(doc, null)
                    saveTicket(receiptText, (order.id + order.others.dirSuffix))
                }
                if(printForcefully){
                    printJob.print(doc, null)
                    this.printEnd()
                }
            } catch (e: PrintException) {
                e.printStackTrace()
            }
        }
    }

    fun prepareReceipt(order: OrderDetail, printForcefully: Boolean = false){

        var products = ""
        order.product.forEach {
            products += "${it.name} (x${it.quantity})"
        }

        var address = ""
        address += order.shipping.address1
        if(order.shipping.address2.isNotEmpty()){
            address += order.shipping.address2
        }

        var orderNote = "";
        if(order.others.note.isNotEmpty()){
            orderNote = """
Order Note:
${order.others.note}
            """
        }

        val receiptText = """
Order Info

Order Number: ${order.id}

Shipping Information:
${order.others.shippingMethod}
Shipping Charge: ${order.others.shippingTotal}

Customer Contact:
${order.billing.email}
${order.billing.phone}

Order Status: PAID
Order On: ${order.others.orderDateCreated}

Items(s):
$products

Shipping Address:

${order.shipping.firstName} ${order.shipping.lastName}
$address
${order.shipping.city} - ${order.shipping.postcode}
${order.shipping.state} - ${order.others.shippingCountry}

$orderNote

Payment Method:
${order.others.paymentMethod}

Subtotal: ${order.others.shippingTotal}
Taxes: ${order.others.shippingTax}
Total Order: ${order.others.orderTotal}

---------------------------

        """
        printReceipt(receiptText, order, printForcefully)
    }

    fun printEnd(){
        printService?.let{
            val printJob = it.createPrintJob()
            val bytes = byteArrayOf(27, 100, 3)
            val flavor: DocFlavor = DocFlavor.BYTE_ARRAY.AUTOSENSE
            val doc: Doc = SimpleDoc(bytes, flavor, null)
            printJob.print(doc, null)
        }
    }
}