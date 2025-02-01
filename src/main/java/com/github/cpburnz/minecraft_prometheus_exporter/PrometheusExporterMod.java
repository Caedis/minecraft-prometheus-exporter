package com.github.cpburnz.minecraft_prometheus_exporter;

import java.io.IOException;
import java.net.BindException;

import net.minecraft.server.MinecraftServer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.cpburnz.minecraft_prometheus_exporter.Collectors.Chunks;
import com.github.cpburnz.minecraft_prometheus_exporter.Collectors.Entities;
import com.github.cpburnz.minecraft_prometheus_exporter.Collectors.Players;
import com.github.cpburnz.minecraft_prometheus_exporter.Collectors.Teams;
import com.github.cpburnz.minecraft_prometheus_exporter.Collectors.Ticks;
import com.github.cpburnz.minecraft_prometheus_exporter.Collectors.TileEntities;
import com.github.cpburnz.minecraft_prometheus_exporter.prometheus_exporter.Tags;
import com.gtnewhorizon.gtnhlib.config.ConfigException;
import com.gtnewhorizon.gtnhlib.config.ConfigurationManager;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;
import cpw.mods.fml.relauncher.Side;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.HTTPServer;
import io.prometheus.client.hotspot.DefaultExports;

/**
 * The PrometheusExporterMod class defines the mod.
 */
@Mod(
    modid = PrometheusExporterMod.MODID,
    version = Tags.VERSION,
    name = PrometheusExporterMod.NAME,
    acceptedMinecraftVersions = "[1.7.10]",
    acceptableRemoteVersions = "*")
public class PrometheusExporterMod {

    public static final String NAME = "Prometheus Exporter";
    public static final String MODID = "prometheus_exporter";

    /**
     * The logger to use.
     */
    public static final Logger LOG = LogManager.getLogger(MODID);

    /**
     * The HTTP server.
     */
    private HTTPServer http_server;

    /**
     * The Minecraft server.
     */
    private MinecraftServer mc_server;

    /**
     * Unregister the metrics collectors.
     */
    private void closeCollectors() {
        // Unregister all collectors.
        CollectorRegistry.defaultRegistry.clear();
    }

    /**
     * Stop the HTTP server.
     */
    private void closeHttpServer() {
        // WARNING: Remember to stop the HTTP server. Otherwise, the Minecraft
        // client will crash because the TCP port will already be in use when trying
        // to load a second saved world.
        this.http_server.close();
    }

    /**
     * Register the metrics collectors.
     */
    private void initCollectors() {
        // Collect JVM stats.
        if (ExporterConfig.collector.jwm_collector) DefaultExports.initialize();

        if (ExporterConfig.collector.entities) new Entities(this.mc_server).register();
        if (ExporterConfig.collector.tileentities) new TileEntities(this.mc_server).register();
        if (ExporterConfig.collector.ticks) new Ticks(this.mc_server).register();
        if (ExporterConfig.collector.chunks) new Chunks(this.mc_server).register();
        if (ExporterConfig.collector.players) new Players(this.mc_server).register();
        if (ExporterConfig.collector.teams && ModCompat.ServerUtilities.isLoaded())
            new Teams(this.mc_server).register();
    }

    /**
     * Start the HTTP server.
     */
    private void initHttpServer() throws IOException {
        // WARNING: Make sure the HTTP server thread is daemonized, otherwise the
        // Minecraft server process will not properly terminate.
        String address = ExporterConfig.web.listen_address;
        int port = ExporterConfig.web.listen_port;
        try {
            this.http_server = new HTTPServer(address, port, true);
            LOG.info("Listening on {}:{}", address, port);
        } catch (BindException e) {
            LOG.error("Failed to start prometheus exporter, port " + port + " already in use.");
        }
    }

    /**
     * Called before any other phase. Configuration files should be read.
     *
     * @param event The event.
     */
    @Mod.EventHandler
    public void onPreInitialization(FMLPreInitializationEvent event) {
        if (event.getSide() == Side.CLIENT) return;

        // Register the server config.
        try {
            ConfigurationManager.registerConfig(ExporterConfig.class);
        } catch (ConfigException e) {
            throw new RuntimeException(e);
        }
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        // Register server commands in this event handler.

        // Record the Minecraft server.
        this.mc_server = event.getServer();
    }

    @Mod.EventHandler
    public void onServerStarted(FMLServerStartedEvent event) throws IOException {
        // Initialize HTTP server.
        this.initHttpServer();

        // Initialize collectors.
        this.initCollectors();
    }

    @Mod.EventHandler
    public void onServerStopped(FMLServerStoppedEvent event) {
        // Close collectors.
        this.closeCollectors();

        // Stop HTTP server.
        this.closeHttpServer();
        LOG.info("Closing http server");
    }
}
