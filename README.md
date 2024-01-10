# Git Library

A gradle library implemented in Kotlin that imitates essential functionalities of a Git system.

It consists of three main models:
- Blob
- Tree
- Commit

API:
- `createCommit(tree: Tree, author: String, message: String)`: Creates a new commit.
- `listCommits(): List<Commit>`: Lists all commits chronologically.
- `findCommitByHash(hash: String): Commit?`: Finds a commit by its SHA-1 hash.
- `findCommitsByAuthor(author: String): List<Commit>`: Searches for commits by author.
- `findCommitsByMessage(message: String): List<Commit>`: Searches for commits by message.
- `findCommitsBeforeTimestamp(timestamp: Long): List<Commit>`: Finds commits before a specified timestamp.
- `findCommitsAfterTimestamp(timestamp: Long): List<Commit>`: Finds commits after a specified timestamp.
- `getBlobEntriesSize(): Int`: Retrieves the size of unique Blob entries.
- `createBranch(name: String)`: Creates a new branch with the specified name. Throws IllegalStateException if there is no initial commit on default branch.
- `getBranches(): Set<String>`: Retrieves the names of all created branches.
- `switchToBranch(name: String)`: Switches to the specified branch. Similar to `git checkout`. Throws IllegalArgumentException if there is no branch with the given name.

The library adopts a strategy to prevent data redundancy. It achieves this by storing only unique Blobs and referencing existing Blobs when a newly committed Blob shares the same data hash as a previous one. It also supports branching with the default branch being referred to as `master`.
