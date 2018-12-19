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

import de.flapdoodle.embed.process.config.IRuntimeConfig;
import de.flapdoodle.embed.process.distribution.BitSize;
import de.flapdoodle.embed.process.distribution.Distribution;
import de.flapdoodle.embed.process.distribution.IVersion;
import de.flapdoodle.embed.process.distribution.Platform;
import de.flapdoodle.embed.process.extract.IExtractedFileSet;
import de.flapdoodle.embed.redis.config.RedisDConfig;
import de.flapdoodle.embed.redis.config.RuntimeConfigBuilder;
import de.flapdoodle.embed.redis.distribution.Version;
import junit.framework.TestCase;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// CHECKSTYLE:OFF
public class RedisRuntimeTest extends TestCase {

    public void testNothing() {

    }

    public void testSingleVersion() throws IOException {

        RuntimeConfigBuilder defaultBuilder = new RuntimeConfigBuilder()
                .defaults(Command.RedisD);

        IRuntimeConfig config = defaultBuilder.build();

        check(config, new Distribution(Version.STABLE, Platform.OS_X, BitSize.B32));
    }

    public void testDistributions() throws IOException {
        RuntimeConfigBuilder defaultBuilder = new RuntimeConfigBuilder()
                .defaults(Command.RedisD);

        IRuntimeConfig config = defaultBuilder.build();

        for (Platform platform : Platform.values()) {
            if (platform == Platform.Solaris
                    || platform == Platform.FreeBSD
                    || platform == Platform.Windows) {
                continue;
            }
            for (IVersion version : Versions
                    .testableVersions(Version.class)) {
                int numberChecked = 0;
                for (BitSize bitsize : BitSize.values()) {
                    numberChecked++;
                    check(config, new Distribution(
                            version, platform,
                            bitsize));
                }
                assertTrue(numberChecked > 0);
            }
        }
    }

    private void check(IRuntimeConfig runtime, Distribution distribution)
            throws IOException {
        assertTrue("Check",
                runtime.getArtifactStore().checkDistribution(
                        distribution));
        IExtractedFileSet files = runtime.getArtifactStore()
                .extractFileSet(distribution);
        assertNotNull("Extracted", files.executable());
        assertTrue("Delete", files.executable().delete());
    }

    public void testCheck() throws IOException, InterruptedException {

        Timer timer = new Timer();

        int port = 12345;
        RedisDProcess redisdProcess = null;
        RedisDExecutable redisd = null;

        IRuntimeConfig runtimeConfig = new RuntimeConfigBuilder().defaults(
                Command.RedisD).build();
        RedisDStarter runtime = RedisDStarter.getInstance(runtimeConfig);

        timer.check("After Runtime");

        try {
            redisd = runtime.prepare(new RedisDConfig(
                    Version.STABLE, port));
            timer.check("After redisd");
            assertNotNull("redisd", redisd);
            redisdProcess = redisd.start();
            timer.check("After redisdProcess");

            Jedis jedis = new Jedis("localhost", 12345);
            timer.check("After jedisd");
            // adding a new key
            jedis.set("key", "value");
            timer.check("After jedis store");
            // getting the key value
            assertEquals("value", jedis.get("key"));
            timer.check("After jedis get");
        } finally {
            if (redisdProcess != null)
                redisdProcess.stop();
            timer.check("After redisdProcess stop");
            if (redisd != null)
                redisd.stop();
            timer.check("After redisd stop");
        }
        timer.log();
    }

    static class Timer {

        long _start = System.currentTimeMillis();
        long _last = _start;

        List<String> _log = new ArrayList<String>();

        void check(String label) {
            long current = System.currentTimeMillis();
            long diff = current - _last;
            _last = current;

            _log.add(label + ": " + diff + "ms");
        }

        void log() {
            for (String line : _log) {
                System.out.println(line);
            }
        }
    }

}
