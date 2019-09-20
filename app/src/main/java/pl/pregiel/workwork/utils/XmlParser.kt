package pl.pregiel.workwork.utils

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import pl.pregiel.workwork.data.pojo.Tax
import java.io.InputStream
import java.net.URL

class XmlParser {

    private val kotlinXmlMapper = XmlMapper(JacksonXmlModule().apply {
        setDefaultUseWrapper(false)
    }).registerKotlinModule()
            .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)


    private inline fun <reified T : Any> parseAs(resource: URL): T {
        return kotlinXmlMapper.readValue(resource)
    }

    fun parse(stream: InputStream) : Tax {
        return kotlinXmlMapper.readValue(stream)
    }
}
