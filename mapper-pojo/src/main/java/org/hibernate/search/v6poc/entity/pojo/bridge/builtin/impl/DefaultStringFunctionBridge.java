/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.v6poc.entity.pojo.bridge.builtin.impl;

import org.hibernate.search.v6poc.backend.document.model.IndexSchemaFieldContext;
import org.hibernate.search.v6poc.backend.document.model.IndexSchemaFieldTypedContext;
import org.hibernate.search.v6poc.entity.pojo.bridge.FunctionBridge;

public final class DefaultStringFunctionBridge implements FunctionBridge<String, String> {

	@Override
	public IndexSchemaFieldTypedContext<String> bind(IndexSchemaFieldContext context) {
		return context.asString();
	}

	@Override
	public String toIndexedValue(String propertyValue) {
		return propertyValue;
	}

}