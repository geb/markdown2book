package markdown2book

class GenerationException extends Exception {

	GenerationException(message, cause = null) {
		super(message.toString(), cause)
	}

}