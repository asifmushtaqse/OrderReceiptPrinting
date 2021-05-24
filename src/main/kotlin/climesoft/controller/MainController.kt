package climesoft.controller

import climesoft.ui.TableColumns
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.TableView
import javafx.scene.control.TextField
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import climesoft.modal.OrderDetail
import climesoft.service.ApiService
import climesoft.util.Configuration
import java.util.concurrent.TimeUnit

class MainController {

    @FXML
    var ordersTable: TableView<OrderDetail>? = null
    @FXML
    var pathField: TextField? = null
    @FXML
    var timeField: TextField? = null


    private var data: ObservableList<OrderDetail>? = null
    private val columns: TableColumns = TableColumns()
    private val apiService = ApiService()
    private val configuration = Configuration.instance

    @FXML
    fun initialize() {
        pathField?.text = configuration.getRootPath()
        timeField?.text = configuration.getRefreshTime().toString()
        ordersTable?.columnResizePolicy = TableView.CONSTRAINED_RESIZE_POLICY
        ordersTable?.columns?.add(columns.id())
        ordersTable?.columns?.add(columns.product())
        ordersTable?.columns?.add(columns.shipping())
        ordersTable?.columns?.add(columns.shippingMethod())
        ordersTable?.columns?.add(columns.billing())
        ordersTable?.columns?.add(columns.status())
        ordersTable?.columns?.add(columns.action())
        data = FXCollections.observableArrayList(apiService.getData())
        ordersTable?.items = data
    }

    init {
        GlobalScope.launch(Dispatchers.IO) {
            refreshOrders()
        }
    }

    private suspend fun refreshOrders(){
        while(true) {
            delay(TimeUnit.MINUTES.toMillis(configuration.getRefreshTime()))
            data = FXCollections.observableArrayList(apiService.getData())
        }
    }

    fun updateConfig(event: ActionEvent){
        val rootPath = pathField?.text.toString()
        val refreshTime = timeField?.text?.toLong()
        configuration.saveData(rootPath, refreshTime!!)
    }
}