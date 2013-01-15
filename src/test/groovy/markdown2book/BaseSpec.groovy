package markdown2book

import spock.lang.*

abstract class BaseSpec extends Specification {
	
	def createDestination(book) {
		// This is no good, but unsure of a way to safely get to the target dir
		def destination = new File("target/books", book)
		if (destination.exists()) {
			assert destination.deleteDir()
		}
		assert destination.mkdirs()
		destination
	}
	
	def generate(book) {
		def destination = createDestination(book)
		def generator = createGenerator(book, destination)
		generator.generate()
		destination
	}
	
	def createGenerator(book, destination) {
		def source = new File(this.class.classLoader.getResource("books/$book").toURI())
		assert source.exists()
		new Generator(source, destination)
	}
	
}