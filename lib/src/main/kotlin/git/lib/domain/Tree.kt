package git.lib.domain

import git.lib.util.HashUtils

class Tree {
    val entries: HashMap<String, Any> = HashMap()
    val hash = HashUtils.calculateSHA1(entries.toString())

    fun addEntry(name: String, entry: Any) {
        entries[name] = entry
    }
}