package climesoft.service

import climesoft.modal.OrderDetail
import javax.print.*
import javax.print.event.PrintJobEvent
import javax.print.event.PrintJobListener

class PrintingService {

    private val printService: PrintService? = PrintServiceLookup.lookupDefaultPrintService()

    companion object {
        val instance = PrintingService()
    }

    fun printServiceAvailable() = printService != null

    private fun printReceipt(receiptText: String, order: OrderDetail) {
        if (printService != null) {
            val printJob = printService.createPrintJob()
            val doc: Doc = SimpleDoc(receiptText, DocFlavor.STRING.TEXT_PLAIN, null)
            try {
                printJob.print(doc, null)
                printJob.addPrintJobListener(
                        object : PrintJobListener{
                            override fun printDataTransferCompleted(pje: PrintJobEvent?) {
                                TODO("Not yet implemented")
                            }

                            override fun printJobCompleted(pje: PrintJobEvent?) {
                                TODO("Not yet implemented")
                            }

                            override fun printJobFailed(pje: PrintJobEvent?) {
                                TODO("Not yet implemented")
                            }

                            override fun printJobCanceled(pje: PrintJobEvent?) {
                                TODO("Not yet implemented")
                            }

                            override fun printJobNoMoreEvents(pje: PrintJobEvent?) {
                                TODO("Not yet implemented")
                            }

                            override fun printJobRequiresAttention(pje: PrintJobEvent?) {
                                TODO("Not yet implemented")
                            }

                        }
                )
            } catch (e: PrintException) {
                e.printStackTrace()
            }
        }
    }

    fun prepareReceipt(order: OrderDetail){

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
        printReceipt(receiptText, order)
    }
}