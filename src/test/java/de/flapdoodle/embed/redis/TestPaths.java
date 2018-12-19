/**
 * Copyright (C) 2011
 * Michael Mosmann <michael@mosmann.de>
 * Martin Jöhren <m.joehren@googlemail.com>
 * <p>
 * with contributions from
 * konstantin-ba@github, Archimedes Trajano (trajano@github), Christian Bayer (chrbayer84@googlemail.com)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.flapdoodle.embed.redis;

import de.flapdoodle.embed.process.distribution.BitSize;
import de.flapdoodle.embed.process.distribution.Distribution;
import de.flapdoodle.embed.process.distribution.Platform;
import de.flapdoodle.embed.redis.distribution.Version;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TestPaths {

    private Paths paths;

    @Before
    public void setUp() throws Exception {
        paths = new Paths(Command.RedisD);
    }

    @Test
    public void testDistributionPathsLinux() {
        checkPath(new Distribution(Version.V5, Platform.Linux,
                        BitSize.B64),
                "/5.0.3/redis-server-linux-5.0.3.tar.gz");
        // TODO create test that windows isn't supported
//		checkPath(new Distribution(Version.V5, Platform.Windows,
//				BitSize.B32), "/5.0.3/redis-windows-5.0.3.zip");
        checkPath(new Distribution(Version.V5, Platform.OS_X,
                        BitSize.B64),
                "/5.0.3/redis-server-osx-5.0.3.tar.gz");
    }

    private void checkPath(Distribution distribution, String match) {
        assertEquals("" + distribution, match, paths.getPath(distribution));
    }

    @Test
    public void testPaths() {
        for (Version v : Version.values()) {
            assertNotNull("" + v, Paths.getVersionPart(v));
        }
    }

}
