/*
 * Copyright 2012-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.snowstore.mercury.core.indicator.impl;

import java.io.File;

/**
 * External configuration properties for {@link DiskSpaceHealthIndicator}
 * 
 * @author Andy Wilkinson
 * @since 1.2.0
 */
public class DiskSpaceHealthIndicatorProperties {

	private static final int MEGABYTES = 1024 * 1024;

	/**
	 * Path used to compute the available disk space.
	 */
	private File path = new File(".");

	/**
	 * Minimum disk space that should be available, in bytes.
	 */

	public File getPath() {
		return this.path;
	}

	public void setPath(File path) {
		this.path = path;
	}

}
