package git.lib.domain

import git.lib.util.HashUtils

class Blob(val data: String) {
    val hash = HashUtils.calculateSHA1(data)
}