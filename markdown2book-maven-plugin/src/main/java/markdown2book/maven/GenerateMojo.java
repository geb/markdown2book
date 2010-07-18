package markdown2book.maven;

import markdown2book.Generator;
import markdown2book.GenerationException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import java.io.File;

/**
 * @goal generate
 */
public class GenerateMojo extends AbstractMojo {
	
	/**
	 * The directory containing the source.
	 *
	 * @parameter default-value="src/book"
	 */
	private File source;

	/**
	 * The directory to place the generated content in.
	 *
	 * @parameter default-value="${project.build.directory}/book"
	 */
	private File destination;

	public void execute() throws MojoExecutionException {
		if (!source.exists()) {
			getLog().warn("Skipping Markdown2Book generation due to source '" + source + "' not existing");
			return;
		}
		try {
			new Generator(source, destination).generate();
			getLog().info("Markdown2Book generated book from '" + source.getPath() + "' to '" + destination.getPath() + "'");
		} catch (GenerationException e) {
			throw new MojoExecutionException("Markdown2Book generation failed", e);
		}
	}
}
