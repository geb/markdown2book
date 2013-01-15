package markdown2book

class OriginalExampleSpec extends BaseSpec {

	def generate() {
		when:
		generate("original-example")
		then:
		notThrown(Exception)
	}

}