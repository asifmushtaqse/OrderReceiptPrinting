package climesoft.util

import java.io.File

class Configuration {

    private var rootPath: String
    private var refreshTime: Int
    val fileName = javaClass.getResource("/config.txt")

    init {
        val configFile = File(fileName.toURI())
        configFile.bufferedReader().use {
            val pathTime = it.readText().split(";")
            this.rootPath = pathTime[0]
            this.refreshTime = pathTime[1].toInt()
        }
    }

    fun getRootPath() = this.rootPath
    fun getRefreshTime() = this.refreshTime

    fun saveData(path: String, time: Int){
        val configFile = File(fileName.toURI())
        configFile.bufferedWriter().use { out ->
            out.write("$path;$time")
            out.flush()
        }
        this.rootPath = path
        this.refreshTime = time
    }

    companion object {
        val instance = Configuration()
    }
}