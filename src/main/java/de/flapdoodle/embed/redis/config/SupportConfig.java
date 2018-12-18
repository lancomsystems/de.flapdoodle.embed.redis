/**
 * Copyright (C) 2011
 *   Michael Mosmann <michael@mosmann.de>
 *   Martin Jöhren <m.joehren@googlemail.com>
 *
 * with contributions from
 * 	konstantin-ba@github, Archimedes Trajano (trajano@github), Christian Bayer (chrbayer84@googlemail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.flapdoodle.embed.redis.config;

import de.flapdoodle.embed.process.config.ISupportConfig;
import de.flapdoodle.embed.redis.Command;

public class SupportConfig implements ISupportConfig {
	private final Command command;

	public SupportConfig(Command command) {
		this.command = command;
	}

	@Override
	public String getName() {
		return command.commandName();
	}

	@Override
	public String getSupportUrl() {
		return "https://github.com/flapdoodle-oss/de.flapdoodle.embed.redis/issues\n";
	}

	@Override
	public String messageOnException(Class<?> context, Exception exception) {
		return null;
	}

	@Override
	public long maxStopTimeoutMillis() {
		return 0;
	}
}
