package climesoft.service

import com.beust.klaxon.Klaxon
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import climesoft.modal.OrderDetail
import climesoft.util.Configuration
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import kotlin.collections.ArrayList


class ApiService {

    val configuration = Configuration.instance
    val printServices = PrintingService.instance

    @DelicateCoroutinesApi
    fun getData(): ArrayList<OrderDetail> {
        val input = getConnectFromUrl("https://freesnaps.co.uk/orders.php")
        val reader = BufferedReader(InputStreamReader(input))
        val jsonString = reader.readLine()
        val json = Klaxon().parseArray<OrderDetail>(jsonString)
        handleImages(json)
        printOrders(json)
        return json as ArrayList<OrderDetail>
    }

    private fun handleImages(json: List<OrderDetail>?) {
        val middlePoint = json?.size?.div(2)
        val firstHalf = middlePoint?.let { json.subList(0, it) }
        val secondHalf = middlePoint?.let { json.subList(it, json.size) }
        GlobalScope.launch(IO) { // launch coroutine in the main thread
            downloadImages(firstHalf)
        }
        GlobalScope.launch(IO) { // launch coroutine in the main thread
            downloadImages(secondHalf)
        }
    }

    private fun printOrders(orders: List<OrderDetail>?){
        if(printServices.printServiceAvailable()){
            GlobalScope.launch(IO) {
                orders?.forEach {
                    printServices.prepareReceipt(it)
                }
            }
        }
    }


    private fun downloadImages(orders: List<OrderDetail>?){
        orders?.forEach { orderDetail ->
            orderDetail.product.forEach { product ->
                product.uploads.forEach { upload ->
                    val mainDir = (orderDetail.id + orderDetail.others.dirSuffix)
                    val productDir = "x${upload.quantity} ${product.name}"
                    val finalDir = "$mainDir/$productDir"
                    saveImage(finalDir, upload.image!!)
                }
            }
        }
    }

    private fun saveImage(folder: String, imageLink: String){
        val folderPath = configuration.getRootPath() + folder

        val directory = File(folderPath)
        if (!directory.exists()) {
            directory.mkdirs()
        }
        val fileName = imageLink.substring(imageLink.lastIndexOf('/') + 1);
        val filePath = File("$folderPath/$fileName")
        saveImageToDisk(imageLink, filePath)
    }

    private fun saveImageToDisk(imageLink: String, filePath: File){
        if(Files.notExists(Paths.get(filePath.absolutePath))) {
            getConnectFromUrl(imageLink).use { inputStream ->
                Files.copy(
                        inputStream,
                        Paths.get(filePath.absolutePath),
                        StandardCopyOption.REPLACE_EXISTING
                )
            }
        }
    }

    private fun getConnectFromUrl(link: String): InputStream{
        val url = URL(link)
        val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
        connection.addRequestProperty("User-Agent", "Mozilla/4.0")
        connection.connect()
        return if (connection.responseCode == 200) connection.inputStream else connection.errorStream
    }

}