package climesoft.util

import java.io.File

class Configuration {

    private var rootPath: String
    private var refreshTime: Long
    private val configFilePath = getRootDir() + "/config.txt"

    init {
        val configFile = File(configFilePath)
        configFile.bufferedReader().use {
            val pathTime = it.readText().split(";")
            this.rootPath = pathTime[0]
            this.refreshTime = pathTime[1].toLong()
        }
    }

    fun getRootPath() = this.rootPath
    fun getRefreshTime() = this.refreshTime

    fun saveData(path: String, time: Long){
        val configFile = File(configFilePath)
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