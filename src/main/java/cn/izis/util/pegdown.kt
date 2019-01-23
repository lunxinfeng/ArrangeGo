package cn.izis.util

import java.io.File
import org.pegdown.PegDownProcessor
import okio.Okio
import java.io.InputStream
import java.nio.charset.Charset


fun mdToHtml(
    mdFile:File
): String {
    val bufferSource = Okio.buffer(Okio.source(mdFile))
    val content = bufferSource.readString(Charset.forName("utf-8"))
    bufferSource.close()

    val pdp = PegDownProcessor(Integer.MAX_VALUE)
    return pdp.markdownToHtml(content)
}

fun mdToHtml(
    inputStream: InputStream
): String {
    val bufferSource = Okio.buffer(Okio.source(inputStream))
    val content = bufferSource.readString(Charset.forName("utf-8"))
    bufferSource.close()

    val pdp = PegDownProcessor(Integer.MAX_VALUE)
    return pdp.markdownToHtml(content)
}