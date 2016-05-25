package cedia.ncde.io.store

import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*

/**
 * Created on 2016-05-23.
 */
object NPCNamesStore {

    private val names: HashMap<Int, String> = HashMap()

    fun init() {
        BufferedReader(InputStreamReader(NPCNamesStore.javaClass.classLoader.getResourceAsStream("npcList.txt"))).use {
            do {
                val line = it.readLine() ?: break
                if (!line.startsWith("//")) {
                    val splitLine = line.split(" - ".toRegex(), 3).toTypedArray()
                    val id = Integer.valueOf(splitLine[0])!!
                    val name = splitLine[1]
                    names.put(id, name)
                }
            } while (true)
        }
    }

    fun getNameForId(id: Int): String {
        val name = names.getOrDefault(id, "Unknown")
        return if (name.isBlank()) "Unknown" else name
    }

    fun getNames(): Map<Int, String> {
        return Collections.unmodifiableMap(names)
    }
}