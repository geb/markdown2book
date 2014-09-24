package markdown2book

import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.*

import java.nio.charset.Charset

abstract class BaseSpec extends Specification {

    @Rule
    TemporaryFolder destRoot;

	def createDestination(book) {
		// This is no good, but unsure of a way to safely get to the target dir
		def destination = destRoot.newFolder(book)
		if (destination.exists()) {
			assert destination.deleteDir()
		}
		assert destination.mkdirs()
		destination
	}
	
	def generate(book, extensions = null) {
		def destination = createDestination(book)
		def generator = createGenerator(book, destination, extensions)
		generator.generate()
		destination
	}
	
	def createGenerator(book, destination, extensions) {
		def source = new File(this.class.classLoader.getResource("books/$book").toURI())
		assert source.exists()
		extensions == null ? new Generator(source, destination) : new Generator(source, destination, Charset.defaultCharset().name(), extensions)
	}
	
}