/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package git.lib

import git.lib.domain.Blob
import git.lib.domain.Tree
import org.junit.jupiter.api.TestInstance
import kotlin.test.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GitTest {
    private val author = "Vladimir Popov"
    private val message = "Sample message"

    @Test
    fun testCreateCommitAndListCommits() {
        val git = Git()
        val workTree = Tree()
        val blob1 = Blob("sample data1")
        val blob2 = Blob("sample data2")
        val blob3 = Blob("sample data3")
        val tree1 = Tree()
        val tree2 = Tree()
        tree1.addEntry("blob1", blob1)
        tree2.addEntry("tree1", tree1)
        tree2.addEntry("blob2", blob2)
        workTree.addEntry("tree2", tree2)
        workTree.addEntry("blob3", blob3)

        git.createCommit(workTree, author, message)
        val commits = git.listCommits()

        assertEquals(commits.size, 1)
        assertEquals(git.getBlobEntriesSize(), 3)
        assertEquals(commits[0].author, author)
        assertEquals(commits[0].message, message)
    }

    @Test
    fun testFindCommitByHash() {
        val git = Git()
        val tree = Tree()
        git.createCommit(tree, author, message)

        val commitHash = git.listCommits()[0].hash
        val foundCommit = git.findCommitByHash(commitHash)

        assertNotNull(foundCommit)
        assertEquals(commitHash, foundCommit.hash)
    }

    @Test
    fun testFindCommitByAuthor() {
        val git = Git()
        val tree = Tree()
        git.createCommit(tree, author, message)
        git.createCommit(tree, author + 1, message + 1)
        git.createCommit(tree, author, message + 2)

        val foundCommits = git.findCommitsByAuthor(author)
        val foundCommits1 = git.findCommitsByAuthor(author + 1)

        assertEquals(foundCommits.size, 2)
        assertEquals(foundCommits1.size, 1)
        foundCommits.forEach { commit -> assertEquals(commit.author, author) }
        foundCommits1.forEach { commit -> assertEquals(commit.author, author + 1) }
    }

    @Test
    fun testFindCommitsByMessage() {
        val git = Git()
        val tree = Tree()
        git.createCommit(tree, author, "Fix: $message")
        git.createCommit(tree, author, "Fix: $message 1")
        git.createCommit(tree, author, message)

        val foundCommits = git.findCommitsByMessage("Fix")

        assertEquals(foundCommits.size, 2)
        foundCommits.forEach { commit -> commit.message.contains("Fix")}
    }

    @Test
    fun testFindCommitsBeforeTimestamp() {
        val git = Git()
        val tree = Tree()
        git.createCommit(tree, author, message)
        git.createCommit(tree, author, message)
        Thread.sleep(10)
        val timestamp = System.currentTimeMillis()
        git.createCommit(tree, author, message)

        val foundCommits = git.findCommitsBeforeTimestamp(timestamp)

        assertEquals(foundCommits.size, 2)
        foundCommits.forEach { commit -> assertTrue { commit.timestamp < timestamp } }
    }

    @Test
    fun testFindCommitsAfterTimestamp() {
        val git = Git()
        val tree = Tree()
        git.createCommit(tree, author, message)
        git.createCommit(tree, author, message)
        val timestamp = System.currentTimeMillis()
        Thread.sleep(10)
        git.createCommit(tree, author, message)

        val foundCommits = git.findCommitsAfterTimestamp(timestamp)

        assertEquals(foundCommits.size, 1)
        foundCommits.forEach { commit -> assertTrue { commit.timestamp > timestamp } }
    }

    @Test
    fun testRedundantData() {
        val git = Git()
        val workTree = Tree()
        val blob1 = Blob("sample data1")
        val blob2 = Blob("sample data2")
        val blob3 = Blob("sample data3")
        val tree1 = Tree()
        val tree2 = Tree()
        tree1.addEntry("blob1", blob1)
        tree2.addEntry("tree1", tree1)
        tree2.addEntry("blob2", blob2)
        workTree.addEntry("tree2", tree2)
        workTree.addEntry("blob3", blob3)
        git.createCommit(workTree, author, message)

        val workTree1 = Tree()
        val blob11 = Blob("sample data1")
        val blob21 = Blob("sample data2")
        workTree1.addEntry("blob11", blob11)
        workTree1.addEntry("blob21", blob21)
        git.createCommit(workTree1, author, message)

        assertEquals(git.getBlobEntriesSize(), 3)
    }
}
