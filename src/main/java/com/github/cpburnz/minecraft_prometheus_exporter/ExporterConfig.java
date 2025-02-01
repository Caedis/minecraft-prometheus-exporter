package com.github.cpburnz.minecraft_prometheus_exporter;

import com.gtnewhorizon.gtnhlib.config.Config;

/**
 * The Config class defines the mod config. This is used to load and generate
 * the "prometheus_exporter.cfg" config file.
 */
@Config(modid = PrometheusExporterMod.MODID, category = "")
public class ExporterConfig {

    public static Collector collector = new Collector();
    public static Web web = new Web();

    public static class Collector {

        @Config.DefaultBoolean(true)
        @Config.Comment("Whether collecting metrics about the JVM process is enabled")
        public boolean jwm_collector;

        @Config.DefaultBoolean(true)
        @Config.Comment("Whether entity metrics should be collected")
        public boolean entities;

        @Config.DefaultBoolean(true)
        @Config.Comment("Whether tile entity metrics should be collected")
        public boolean tileentities;

        @Config.DefaultBoolean(true)
        @Config.Comment("Whether ticks metrics should be collected")
        public boolean ticks;

        @Config.DefaultBoolean(true)
        @Config.Comment("Whether chunk metrics should be collected")
        public boolean chunks;

        @Config.DefaultBoolean(true)
        @Config.Comment("Whether player metrics should be collected")
        public boolean players;

        @Config.DefaultBoolean(true)
        @Config.Comment("Whether to collect metrics on ServerUtilities teams")
        public boolean teams;
    }

    public static class Web {

        @Config.DefaultString("0.0.0.0")
        @Config.Comment("The default address to listen on. Default is everywhere")
        public String listen_address;

        @Config.DefaultInt(19565)
        @Config.RangeInt(min = 0, max = 65535)
        @Config.Comment({ "The default TCP port ot use.",
            "It was derived from the Minecraft port (25565) and the Prometheus exporter ports (9100+)" })
        public int listen_port;
    }
}
