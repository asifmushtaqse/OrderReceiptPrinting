package climesoft.ui

import javafx.beans.property.SimpleStringProperty
import javafx.event.EventHandler
import javafx.scene.control.Button
import javafx.scene.control.TableCell
import javafx.scene.control.TableColumn
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.layout.VBox
import javafx.util.Callback
import climesoft.modal.OrderDetail
import climesoft.service.PrintingService
import climesoft.util.Configuration
import java.awt.Desktop
import java.io.File

class TableColumns {

    fun id(): TableColumn<OrderDetail, String> {
        val actionCol = TableColumn<OrderDetail, String>("Order#")
        actionCol.setCellValueFactory { cellData -> SimpleStringProperty(
                cellData.value.id + "\n" + cellData.value.others.website
        )}
        return actionCol
    }

    fun product(): TableColumn<OrderDetail, String> {
        val actionCol = TableColumn<OrderDetail, String>("Product")
        actionCol.setCellValueFactory { cellData ->
            var productNames = ""
            cellData.value.product.forEach {
                productNames += it.name + " (Qty: ${it.quantity}) \n"
            }
            SimpleStringProperty(productNames)
        }
        return actionCol
    }

    fun shipping(): TableColumn<OrderDetail, String> {
        val actionCol = TableColumn<OrderDetail, String>("Shipping Address")
        actionCol.setCellValueFactory { cellData ->
            SimpleStringProperty(cellData.value.shipping.toString())
        }
        actionCol.isEditable = true
        return actionCol
    }

    fun shippingMethod(): TableColumn<OrderDetail, String> {
        val actionCol = TableColumn<OrderDetail, String>("Shipping Method")
        actionCol.setCellValueFactory { cellData ->
            SimpleStringProperty(cellData.value.others.shippingMethod)
        }
        return actionCol
    }

    fun billing(): TableColumn<OrderDetail, String> {
        val actionCol = TableColumn<OrderDetail, String>("Billing Address")
        actionCol.setCellValueFactory { cellData ->
            SimpleStringProperty(cellData.value.billing.toString())
        }
        actionCol.isEditable = true
        return actionCol
    }

    fun status(): TableColumn<OrderDetail, String> {
        val actionCol = TableColumn<OrderDetail, String>("Status")
        actionCol.setCellValueFactory { cellData ->
            SimpleStringProperty(cellData.value.others.status)
        }
        return actionCol
    }

    fun action(): TableColumn<OrderDetail, String> {
        val actionCol = TableColumn<OrderDetail, String>("Action")
        actionCol.cellValueFactory = PropertyValueFactory("DUMMY")
        actionCol.cellFactory = Callback<TableColumn<OrderDetail?, String?>, TableCell<OrderDetail?, String?>> {
            object : TableCell<OrderDetail?, String?>() {
                val btnTicket = Button("Print Ticket")
                val btnFolder = Button("Open Folder")
                val btnImages = Button("Download Images")
                val vBox = VBox()
                init {
                    vBox.children.add(btnTicket)
                    vBox.children.add(btnFolder)
                    vBox.children.add(btnImages)
                    vBox.spacing = 5.0
                }
                override fun updateItem(item: String?, empty: Boolean) {
                    super.updateItem(item, empty)
                    if (empty) {
                        graphic = null
                    } else {
                        val orderItem = tableView.items[index]
                        btnTicket.onAction = EventHandler {
                            PrintingService.instance.prepareReceipt(orderItem!!)
                        }
                        btnFolder.onAction = EventHandler {
                            Desktop.getDesktop().open(File(Configuration.instance.getRootPath() + "/${orderItem?.id}${orderItem?.others?.dirSuffix}"))
                        }
                        btnImages.onAction = EventHandler { }
                        graphic = vBox
                    }
                    text = null
                }
            }
        }
        return actionCol
    }
}