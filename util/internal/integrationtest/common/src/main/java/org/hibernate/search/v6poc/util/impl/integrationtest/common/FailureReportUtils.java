/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.util.impl.integrationtest.common;

import java.lang.annotation.Annotation;

public final class FailureReportUtils {

	private FailureReportUtils() {
	}

	public static SingleContextFailureReportPatternBuilder buildSingleContextFailureReportPattern() {
		return new SingleContextFailureReportPatternBuilder();
	}

	/*
	 * Notes on meta-characters used here:
	 * - "\h" in a regex means "horizontal whitespace characters", i.e. spaces or tabs but not newline
	 * - "\Q" and "\E" in a regex allow to escape all the characters enclosed between them,
	 * which comes in handy to escape user-provided strings
	 * - "." does not match newline characters
	 * - "[\S\s]" matches any character, including newline characters
	 */
	public static class SingleContextFailureReportPatternBuilder {
		private final StringBuilder contextPatternBuilder = new StringBuilder();
		private final StringBuilder failurePatternBuilder = new StringBuilder( "\n\\h+failures: " );

		private SingleContextFailureReportPatternBuilder() {
		}

		public SingleContextFailureReportPatternBuilder typeContext(String exactTypeName) {
			return contextLiteral( "type '" + exactTypeName + "'" );
		}

		public SingleContextFailureReportPatternBuilder pathContext(String pathPattern) {
			return contextLiteral( "path '" + pathPattern + "'" );
		}

		public SingleContextFailureReportPatternBuilder annotationContextAnyParameters(Class<? extends Annotation> annotationType) {
			return contextPattern( "annotation '@\\Q" + annotationType.getName() + "\\E\\(.*'" );
		}

		public SingleContextFailureReportPatternBuilder contextLiteral(String contextLiteral) {
			return contextPattern( "\\Q" + contextLiteral + "\\E" );
		}

		public SingleContextFailureReportPatternBuilder contextPattern(String contextPattern) {
			contextPatternBuilder.append( "\n\\h+" )
					.append( contextPattern )
					.append( ": " );
			return this;
		}

		public SingleContextFailureReportPatternBuilder failure(String ... literalStringsContainedInFailureMessageInOrder) {
			failurePatternBuilder.append( "\n\\h+-\\h" );
			for ( String contained : literalStringsContainedInFailureMessageInOrder ) {
				failurePatternBuilder.append( ".*" )
						.append( "\\Q" ).append( contained ).append( "\\E" );
			}
			failurePatternBuilder.append( ".*" );
			return this;
		}

		public String build() {
			/*
			 * Prepend and append "[\S\s]*" because we have to match against the entire failure report,
			 * so we must match any characters before and after what we're looking for.
			 */
			return "[\\S\\s]*"
					+ contextPatternBuilder.toString()
					+ failurePatternBuilder.toString()
					+ "[\\S\\s]*";
		}
	}

}
