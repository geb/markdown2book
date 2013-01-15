package markdown2book

import org.pegdown.PegDownProcessor
import org.pegdown.Extensions
import java.util.regex.Pattern
import groovy.text.SimpleTemplateEngine
import org.apache.commons.io.FileUtils
import java.nio.charset.Charset

/**
 * @goal generate
 */
class Generator {

	private static MARKDOWN_EXTENSIONS = ['.markdown', '.md', '.mdown']
	private static HEADING_PATTERN = Pattern.compile("^(#+)\\s+(.*)\$")
	private static FILENAME_SUBSTITUTE_PATTERN = Pattern.compile("^(?:\\d+-)?(.*?)\\.(?:markdown|md|mdown)\$")
	private static MARKDOWN_PROCESSOR = new PegDownProcessor(Extensions.ALL)
	private static TEMPLATE_ENGINE = new SimpleTemplateEngine()
	private static TEMPLATE_EXTENSION = "html"
	private static ALL_TEMPLATE = "all." + TEMPLATE_EXTENSION
	private static CHAPTER_TEMPLATE = "chapter." + TEMPLATE_EXTENSION
	private static INDEX_TEMPLATE = "index." + TEMPLATE_EXTENSION
	private static TEMPLATES_DIR_NAME = 'templates'
	private static CHAPTERS_DIR_NAME = 'chapters'
	private static REFERENCES_FILE_NAME = "references.markdown"
	private static DONT_COPY_TO_DESTINATION = [TEMPLATES_DIR_NAME, CHAPTERS_DIR_NAME, REFERENCES_FILE_NAME]
	
	private toc = []
	private chapters = [:]
	private references
	
	final File source
	final File destination
	final String charset
	
	Generator(File source, File destination, String charset = Charset.defaultCharset().name()) {
		if (source == null) {
			fail("The 'source' argument can not be null")
		}
		this.source = source

		if (source == null) {
			fail("The 'destination' argument can not be null")
		}
		this.destination = destination
		this.charset = charset
	}
	
	Generator clean() {
		if (destination.exists()) {
			if (destination.directory) {
				if (!destination.deleteDir()) {
					fail("failed to delete $destination")
				}
			} else {
				fail("$destination exists and is not a directory")
			}
		}
		
		this
	}
	
	Generator generate() throws GenerationException { 
		try {
			doGenerate()
			this
		} catch (Exception e) {
			if (e instanceof GenerationException) {
				throw e
			} else {
				throw new GenerationException("generation failed", e)
			}
		}
	}

	private doGenerate() {
		if (destination.exists()) {
			if (!destination.directory) {
				fail("$destination exists and is not a directory")
			}
		} else if (!destination.mkdirs()) {
			fail("failed to make $destination")
		}
		
		readReferences()
		chapterInputs.eachWithIndex { input, index ->
			processChapter(input, index)
		}
		
		def indexToc = new StringBuilder()
		def allToc = new StringBuilder()
		
		def chapterTemplateBindings = []
		def chapterOutputFilenames = chapters.keySet().toList()
		
		chapterOutputFilenames.each { chapterOutputFilename ->
			def chapter = chapters[chapterOutputFilename]
			def chapterToc = new StringBuilder()
			
			chapter.toc.each {
				indexToc << tocEntryMarkup(it, chapterOutputFilename)
				allToc << tocEntryMarkup(it, "all.html")
				chapterToc << tocEntryMarkup(it, chapterOutputFilename)
			}
			(chapter.toc.last().lastLevel + 1).times { 
				indexToc << "</li></ul>"
				allToc << "</li></ul>"
				chapterToc << "</li></ul>"
			}
			chapterTemplateBindings << createChapterTemplateBinding(chapter, chapterToc)
		}
		
		chapterTemplateBindings.eachWithIndex { binding, i ->
			def filename = chapterOutputFilenames[i]
			def prev = null
			def next = null
			
			if (i == 0) {
				next = chapterTemplateBindings[i + 1]
			} else if (i < (chapterTemplateBindings.size() - 1)) {
				prev = chapterTemplateBindings[i - 1]
				next = chapterTemplateBindings[i + 1]
			} else {
				prev = chapterTemplateBindings[i - 1]
			}
			
			templatise(CHAPTER_TEMPLATE, filename, prev: prev, chapter: binding, next: next)
		}
		
		templatise(INDEX_TEMPLATE, "index.html", toc: indexToc)
		templatise(ALL_TEMPLATE, "all.html", toc: allToc, chapters: chapterTemplateBindings)
		copyOtherFiles()
	}
	
	private createChapterTemplateBinding(chapter, toc) {
		[title: chapter.title, toc: toc, content: chapter.content, filename: chapter.filename]
	}
	
	private getChapterInputs() {
		chaptersSourceDir.listFiles(
			[accept: { File dir, String name -> MARKDOWN_EXTENSIONS.any { name.endsWith(it) }}] as FilenameFilter
		).sort {
			it.name
		}
	}
	
	private readReferences() {
		def referencesFile = new File(source, REFERENCES_FILE_NAME)
		references = referencesFile.exists() ? referencesFile.getText(charset) : ""
	}
	
	private processChapter(input, index) {
		def lastLevel = 0
		def toc = []
		def content = new StringBuilder()
		def firstHeading = null
		def outputFileName = toOutputFileName(input)
		def headingStack = [index]
		
		input.eachLine { line ->
			def heading = parseForHeading(line)
			if (heading) {
				if (firstHeading == null) {
					firstHeading = heading
				}
				if (headingStack.size() < heading.level) {
					headingStack.push(0)
				} else {
					while (headingStack.size() > heading.level) {
						headingStack.pop()
					}
				}
				headingStack.push(headingStack.pop() + 1)
				def prefix = headingStack.join('.')
				def id = toId(heading.title)
				
				toc << [heading: heading, prefix: prefix, id: id, lastLevel: lastLevel]
				
				content << "#" * heading.level
				content << "<span id='"
				content << id
				content << "'>"
				content << prefix
				content << "</span> "
				content << heading.title
				content << "\n"
				
				lastLevel = heading.level
			} else {
				content << line
				content << "\n"
			}
		}
		content << "\n"
		content << references
		
		chapters[outputFileName] = [
			title: firstHeading.title, 
			content: markdown(content.toString()), 
			name: "chapter", 
			toc: toc,
			filename: outputFileName
		]
	}
	
	private parseForHeading(line) {
		def matcher = HEADING_PATTERN.matcher(line)
		if (matcher.matches()) {
			[title: matcher[0][2], level: matcher[0][1].size()]
		} else {
			null
		}
	}

	private tocEntryMarkup(tocEntry, outputFileName) {
		def sb = new StringBuilder()
		if (tocEntry.heading.level > tocEntry.lastLevel) {
			sb << "<ul><li>"
		} else if (tocEntry.heading.level == tocEntry.lastLevel) {
			sb << "</li><li>"
		} else {
			sb << "</li></ul>" * (tocEntry.lastLevel - tocEntry.heading.level)
		}
		sb << "<span class='toc_number'>"
		sb << tocEntry.prefix
		sb << "</span>"
		sb << "<a href='"
		sb << outputFileName
		sb << "#"
		sb << tocEntry.id
		sb << "'>"
		sb << tocEntry.heading.title
		sb << "</a>"
		sb.toString()
	} 
	
	private toOutputFileName(file) {
		def matcher = FILENAME_SUBSTITUTE_PATTERN.matcher(file.name)
		matcher[0][1] + ".html"
	}
	
	private toId(title) {
		title = title.replaceAll('[- \\/_]+', '_')
		title = title.replaceAll('[^a-zA-Z0-9_]+', '')
		title.toLowerCase()
	}
	
	private getOutputFile(filename) {
		def outputFile = new File(destination, filename)
		if (outputFile.exists()) {
			if (!outputFile.delete()) {
				fail("failed to delete $outputFile")
			}
		}
		outputFile.createNewFile()
		outputFile
	}
	
	private markdown(input) {
		MARKDOWN_PROCESSOR.markdownToHtml(input)
	}
	
	private getChaptersSourceDir() {
		def dir = new File(source, CHAPTERS_DIR_NAME)
		if (!dir.exists()) {
			fail("book chapters directory '$dir' does not exist")
		}
		dir
	}
	
	private getTemplatesDir() {
		new File(source, TEMPLATES_DIR_NAME)
	}
	
	private getTemplate(templateName) {
		def template = new File(templatesDir, templateName)
		if (!template.exists()) {
			fail("template '$template' does not exist")
		}
		template
	}
	
	private templatise(Map binding, String templateName, String destination) {
		getOutputFile(destination).withWriter(charset) { templatise(binding, templateName, it) }
	}
	
	private templatise(Map binding, String templateName, Writer writer) {
		def template = getTemplate(templateName)
		TEMPLATE_ENGINE.createTemplate(template.getText(charset)).make(binding).writeTo(writer)
	}
	
	private copyOtherFiles() {
		source.listFiles(
			[accept: { File dir, String name -> DONT_COPY_TO_DESTINATION.every { name != it } }] as FilenameFilter
		).each { toCopy ->
			if (toCopy.directory) {
				FileUtils.copyDirectory(toCopy, new File(destination, toCopy.name))
			} else {
				FileUtils.copyFile(toCopy, new File(destination, toCopy.name))
			}
		}
	}
	
	private fail(message, cause = null) {
		throw new GenerationException(message, cause)
	}
}
