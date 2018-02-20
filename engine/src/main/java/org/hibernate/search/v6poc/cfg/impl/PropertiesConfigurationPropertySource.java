/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.cfg.impl;

import java.util.Optional;
import java.util.Properties;

import org.hibernate.search.v6poc.cfg.ConfigurationPropertySource;

public class PropertiesConfigurationPropertySource implements ConfigurationPropertySource {

	private final Properties properties;

	public PropertiesConfigurationPropertySource(Properties properties) {
		this.properties = properties;
	}

	@Override
	public Optional<?> get(String key) {
		String value = properties.getProperty( key );
		return Optional.ofNullable( value );
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder( getClass().getSimpleName() )
				.append( "[" )
				.append( "properties=" ).append( properties )
				.append( "]" );
		return sb.toString();
	}
}
