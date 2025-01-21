This mod provides a Prometheus exporter for Minecraft. It exports metrics
related to the Minecraft server and the JVM for consumption by the open-source
systems monitoring toolkit, [Prometheus]. The mod is intended for server-side
use, and does not need to be installed client-side. This currently has builds
for the following versions:

- Minecraft 1.7.10 with Forge 10.13.4.

Installation
------------

The Prometheus Exporter mod only needs to be installed on the server. It can be
downloaded from [GitHub]. To install it, copy the JAR
(*Prometheus-Exporter-{MC Version}-forge-{Mod Version}.jar*) to the server
*mods/* directory. Since this mod does not add anything to the Minecraft world,
it can be safely upgraded by simply replacing an older version with a newer
version.


Configuration
-------------

The mod configuration is located at *config/prometheus_exporter.cfg*.
It will be automatically generated upon server start if it does not already
exist. The default configuration can be seen in the example [prometheus_exporter.cfg].


Exporter
--------

The metrics are documented in [metrics.md].

A sample output from the exporter can be seen in the example [output.txt].


Dashboards
----------

Known compatible Grafana dashboards are listed in [dashboards.md].
