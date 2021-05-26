package climesoft.util

import climesoft.Main
import java.io.File
import java.nio.file.Files

fun saveTicket(text: String, orderFolder: String){
    val folderPath = Configuration.instance.getRootPath() + orderFolder
    val filePath = File("$folderPath/ticket.txt")
    if(!Files.exists(filePath.toPath())){
        Files.createDirectories(filePath.toPath().parent)
        Files.createFile(filePath.toPath())
    }
    filePath.bufferedWriter().use { out ->
        out.write(text)
        out.flush()
    }
}

fun ticketExists(path: String): Boolean{
    val ticketPath = Configuration.instance.getRootPath() + path
    return Files.exists(File("$ticketPath/ticket.txt").toPath())
}

fun getRootDir(): String{
    return File(Main::class.java.protectionDomain.codeSource.location.toURI().path).parent
}