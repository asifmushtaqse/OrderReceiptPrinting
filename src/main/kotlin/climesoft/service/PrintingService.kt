package climesoft.service

import climesoft.Main
import climesoft.modal.OrderDetail
import climesoft.util.PrinterOptions
import climesoft.util.saveTicket
import climesoft.util.ticketExists
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.util.*
import javax.print.*
import javax.print.attribute.HashPrintRequestAttributeSet
import javax.print.attribute.PrintRequestAttributeSet
import javax.print.attribute.Size2DSyntax
import javax.print.attribute.standard.JobName
import javax.print.attribute.standard.MediaPrintableArea
import javax.print.attribute.standard.MediaSizeName
import javax.print.attribute.standard.PageRanges


class PrintingService {

    private val printService: PrintService? = PrintServiceLookup.lookupDefaultPrintService()

    companion object {
        val instance = PrintingService()
    }

    fun printServiceAvailable() = printService != null

     private fun printReceipt(receiptText: String, order: OrderDetail?, printForcefully: Boolean = false) {
        if (printService != null) {

            val printJob = printService.createPrintJob()
            val doc: Doc = SimpleDoc( ByteArrayInputStream(receiptText.toByteArray()), DocFlavor.INPUT_STREAM.AUTOSENSE, null)
            try {
                order?.let{
                    if(!ticketExists((order.id + order.others.dirSuffix))){
                        printJob.print(doc, null)
                        saveTicket(receiptText, (order.id + order.others.dirSuffix))
                    }
                }
                if(printForcefully){
                    printJob.print(doc, null)
                }
            } catch (e: PrintException) {
                e.printStackTrace()
            }
        }
    }

    fun prepareReceipt(order: OrderDetail, printForcefully: Boolean = false) {

        val p = PrinterOptions()
        p.resetAll()
        p.initialize()
        p.feedBack(2.toByte())
        p.setText("Order Info")
        p.newLine()
        p.setText("Order Number: ${order.id}")
        p.newLine()
        p.addLineSeperator()
        p.setText("Shipping Information:")
        p.newLine()
        p.setText(order.others.shippingMethod)
        p.newLine()
        p.setText("Shipping Charge: ${order.others.shippingTotal}")
        p.newLine()
        p.addLineSeperator()
        p.setText("Contact Info:")
        p.newLine()
        p.setText(order.billing.email)
        p.newLine()
        p.setText(order.billing.phone)
        p.newLine()
        p.addLineSeperator()
        p.setText("Order Status: PAID")
        p.newLine()
        p.setText("Order On: ${order.others.orderDateCreated}")
        p.newLine()
        p.addLineSeperator()
        p.setText("Items(s):")
        p.newLine()
        order.product.forEach {
            p.setText("${it.name} (x${it.quantity})")
            p.newLine()
        }
        p.addLineSeperator()
        p.setText("Shipping Address:")
        p.newLine()
        p.setText(order.shipping.address1)
        p.newLine()
        p.setText("${order.shipping.firstName} ${order.shipping.lastName}")
        p.newLine()
        if (order.shipping.address2.isNotEmpty()) {
            p.setText(order.shipping.address2)
            p.newLine()
        }
        p.setText("${order.shipping.city} - ${order.shipping.postcode}")
        p.newLine()
        p.setText("${order.shipping.state} - ${order.others.shippingCountry}")
        p.newLine()
        p.addLineSeperator()
        if (order.others.note.isNotEmpty()) {
            p.newLine()
            p.setText("Order Note: ${order.others.note}")
            p.newLine()
            p.addLineSeperator()
        }
        p.setText("Payment Method: ${order.others.paymentMethod}")
        p.newLine()
        p.setText("Subtotal: ${order.others.shippingTotal}")
        p.newLine()
        p.setText("Taxes: ${order.others.shippingTax}")
        p.newLine()
        p.setText("Total Order: ${order.others.orderTotal}")
        p.addLineSeperator()
        p.feed(3.toByte())
        p.finit()
        printReceipt(p.finalCommandSet(), order, printForcefully)

    }
}