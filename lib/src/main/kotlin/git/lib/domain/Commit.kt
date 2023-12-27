package git.lib.domain

import git.lib.util.HashUtils

class Commit(val tree: Tree, val author: String, val message: String) {
    val timestamp = System.currentTimeMillis()
    val hash = HashUtils.calculateSHA1(tree.hash + author + message + timestamp)
}