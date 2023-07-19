/*
Freeware License, some rights reserved

Copyright (c) 2023 Iuliana Cosmina

Permission is hereby granted, free of charge, to anyone obtaining a copy
of this software and associated documentation files (the "Software"),
to work with the Software within the limits of freeware distribution and fair use.
This includes the rights to use, copy, and modify the Software for personal use.
Users are also allowed and encouraged to submit corrections and modifications
to the Software for the benefit of other users.

It is not allowed to reuse,  modify, or redistribute the Software for
commercial use in any way, or for a user's educational materials such as books
or blog articles without prior permission from the copyright holder.

The above copyright notice and this permission notice need to be included
in all copies or substantial portions of the software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS OR APRESS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

*/
package com.apress.prospring6.four

import org.slf4j.LoggerFactory
import org.springframework.beans.PropertyEditorRegistrar
import org.springframework.beans.PropertyEditorRegistry
import org.springframework.beans.factory.annotation.Value
import org.springframework.beans.propertyeditors.CustomDateEditor
import org.springframework.beans.propertyeditors.StringTrimmerEditor
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.stereotype.Component
import java.io.*
import java.net.URL
import java.nio.file.Files
import java.nio.file.Path
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import java.util.stream.Collectors

/**
 * Created by iuliana.cosmina on 26/03/2022
 */
@Component
internal class ValuesHolder(stringList: List<String?>?) {
    var stringList: List<String>
    var inputStream: InputStream? = null

    init {
        this.stringList = java.util.List.of("Mayer", "Psihoza", "Mazikeen")
        try {
            inputStream = FileInputStream(
                System.getProperty("java.io.tmpdir")
                        + System.getProperty("file.separator")
                        + "test.txt"
            )
        } catch (e: FileNotFoundException) {
            e.printStackTrace() // we are not interested in the exception that much
        }
    }
}

@Component("builtInSample")
class DiverseValuesContainer {
    private var bytes // ByteArrayPropertyEditor
            : ByteArray? = null
    private var character //CharacterEditor
            : Char? = null
    private var cls // ClassEditor
            : Class<*>? = null
    private var trueOrFalse // CustomBooleanEditor
            : Boolean? = null
    private var stringList // CustomCollectionEditor
            : List<String>? = null
    private var date // CustomDateEditor
            : Date? = null
    private var floatValue // CustomNumberEditor
            : Float? = null
    private var file // FileEditor
            : File? = null
    private var stream // InputStreamEditor
            : InputStream? = null
    private var locale // LocaleEditor
            : Locale? = null
    private var pattern // PatternEditor
            : Pattern? = null
    private var properties // PropertiesEditor
            : Properties? = null
    private var trimString // StringTrimmerEditor
            : String? = null
    private var url // URLEditor
            : URL? = null

    @Value("A")
    fun setCharacter(character: Char?) {
        LOGGER.info("Setting character: {}", character)
        this.character = character
    }

    @Value("java.lang.String")
    fun setCls(cls: Class<*>) {
        LOGGER.info("Setting class: {}", cls.name)
        this.cls = cls
    }

    @Value("#{systemProperties['java.io.tmpdir']}#{systemProperties['file.separator']}test.txt")
    fun setFile(file: File) {
        LOGGER.info("Setting file: {}", file.absolutePath)
        this.file = file
    }

    @Value("en_US")
    fun setLocale(locale: Locale) {
        LOGGER.info("Setting locale: {}", locale.displayName)
        this.locale = locale
    }

    @Value("name=Ben age=41")
    fun setProperties(properties: Properties) {
        LOGGER.info("Loaded {}", properties.size.toString() + " properties")
        this.properties = properties
    }

    @Value("https://iuliana-cosmina.com")
    fun setUrl(url: URL) {
        LOGGER.info("Setting URL: {}", url.toExternalForm())
        this.url = url
    }

    @Value("John Mayer")
    fun setBytes(vararg bytes: Byte) {
        LOGGER.info("Setting bytes: {}", Arrays.toString(bytes))
        this.bytes = bytes
    }

    @Value("true")
    fun setTrueOrFalse(trueOrFalse: Boolean?) {
        LOGGER.info("Setting Boolean: {}", trueOrFalse)
        this.trueOrFalse = trueOrFalse
    }

    @Value("#{valuesHolder.stringList}")
    fun setStringList(stringList: List<String>?) {
        LOGGER.info("Setting stringList with: {}", stringList)
        this.stringList = stringList
    }

    @Value("20/08/1981")
    fun setDate(date: Date?) {
        LOGGER.info("Setting date: {}", date)
        this.date = date
    }

    @Value("123.45678")
    fun setFloatValue(floatValue: Float?) {
        LOGGER.info("Setting float value: {}", floatValue)
        this.floatValue = floatValue
    }

    @Value("#{valuesHolder.inputStream}")
    fun setStream(stream: InputStream?) {
        this.stream = stream
        LOGGER.info(
            "Setting stream & reading from it: {}",
            BufferedReader(InputStreamReader(stream)).lines().parallel().collect(Collectors.joining("\n"))
        )
    }

    @Value("a*b")
    fun setPattern(pattern: Pattern?) {
        LOGGER.info("Setting pattern: {}", pattern)
        this.pattern = pattern
    }

    @Value("   String need trimming   ")
    fun setTrimString(trimString: String?) {
        LOGGER.info("Setting trim string: {}", trimString)
        this.trimString = trimString
    }

    class CustomPropertyEditorRegistrar : PropertyEditorRegistrar {
        override fun registerCustomEditors(registry: PropertyEditorRegistry) {
            val dateFormatter = SimpleDateFormat("MM/dd/yyyy")
            registry.registerCustomEditor(
                Date::class.java,
                CustomDateEditor(dateFormatter, true)
            )
            registry.registerCustomEditor(String::class.java, StringTrimmerEditor(true))
        }
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(DiverseValuesContainer::class.java)
        @Throws(Exception::class)
        @JvmStatic
        fun main(args: Array<String>) {
            val baseDir = File(System.getProperty("java.io.tmpdir"))
            val path = Files.createFile(Path.of(baseDir.absolutePath, "test.txt"))
            Files.writeString(path, "Hello World!")
            path.toFile().deleteOnExit()
            val ctx = AnnotationConfigApplicationContext()
            ctx.register(ValuesHolder::class.java, DiverseValuesContainer::class.java)
            ctx.refresh()
            ctx.close()
        }
    }
}