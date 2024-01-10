/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package git.lib

import git.lib.domain.Blob
import git.lib.domain.Commit
import git.lib.domain.Tree

class Git {
    private val blobEntries: HashMap<String, Blob> = HashMap()
    private val branches: HashMap<String, Commit> = HashMap()
    private var head: String = "master"

    private fun processTree(tree: Tree, newEntries: Tree) {
        tree.entries.forEach { entry ->
            if (entry.value is Blob) {
                val blobHash: String = (entry.value as Blob).hash
                if (blobEntries.containsKey(blobHash)) {
                    newEntries.addEntry(entry.key, blobEntries[blobHash]!!)
                } else {
                    newEntries.addEntry(entry.key, entry.value)
                    blobEntries[blobHash] = entry.value as Blob
                }
            } else {
                processTree(entry.value as Tree, newEntries)
            }
        }
    }

    fun createCommit(tree: Tree, author: String, message: String) {
        val newEntries = Tree()
        processTree(tree, newEntries)

        val prevCommit: Commit?
        prevCommit = if (head == "master" && !branches.containsKey(head)) { // initial commit
            null
        } else {
            branches[head]
        }

        val commit = Commit(newEntries, author, message, prevCommit)
        branches[head] = commit
    }

    fun listCommits(): List<Commit> {
        val commits = ArrayList<Commit>()

        var currentCommit = branches[head]
        while (currentCommit != null) {
            commits.add(currentCommit)
            currentCommit = currentCommit.parent
        }

        return commits
    }

    fun findCommitByHash(hash: String): Commit? {
        var currentCommit = branches[head]
        while (currentCommit != null) {
            if (currentCommit.hash == hash)
                return currentCommit
            currentCommit = currentCommit.parent
        }

        return null
    }

    fun findCommitsByAuthor(author: String): List<Commit> {
        val commits = ArrayList<Commit>()

        var currentCommit = branches[head]
        while (currentCommit != null) {
            if (currentCommit.author == author)
                commits.add(currentCommit)
            currentCommit = currentCommit.parent
        }

        return commits
    }

    fun findCommitsByMessage(message: String): List<Commit> {
        val commits = ArrayList<Commit>()

        var currentCommit = branches[head]
        while (currentCommit != null) {
            if (currentCommit.message.contains(message))
                commits.add(currentCommit)
            currentCommit = currentCommit.parent
        }

        return commits
    }

    fun findCommitsBeforeTimestamp(timestamp: Long): List<Commit> {
        val commits = ArrayList<Commit>()

        var currentCommit = branches[head]
        while (currentCommit != null) {
            if (currentCommit.timestamp < timestamp)
                commits.add(currentCommit)
            currentCommit = currentCommit.parent
        }

        return commits
    }

    fun findCommitsAfterTimestamp(timestamp: Long): List<Commit> {
        val commits = ArrayList<Commit>()

        var currentCommit = branches[head]
        while (currentCommit != null) {
            if (currentCommit.timestamp > timestamp)
                commits.add(currentCommit)
            else
                return commits
            currentCommit = currentCommit.parent
        }

        return commits
    }

    fun getBlobEntriesSize(): Int {
        return blobEntries.size
    }

    fun createBranch(name: String) {
        if (head == "master" && !branches.containsKey(head)) {
            throw IllegalStateException("You must create an initial commit first!")
        }

        val startingCommit = branches[head]
        branches[name] = startingCommit!!
    }

    fun getBranches(): Set<String> {
        return branches.keys
    }

    fun switchToBranch(name: String) {
        if (!branches.containsKey(name)) {
            throw IllegalArgumentException("There is no such branch!")
        }

        head = name
    }
}
