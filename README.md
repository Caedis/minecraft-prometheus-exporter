This mod provides a Prometheus exporter for Minecraft. It exports metrics
related to the Minecraft server and the JVM for consumption by the open-source
systems monitoring toolkit, [Prometheus](https://prometheus.io/). The mod is intended for server-side
use, and does not need to be installed client-side. This currently has builds
for the following versions:

- Minecraft 1.7.10 with Forge 10.13.4.

Installation
------------

The Prometheus Exporter mod only needs to be installed on the server.
Since this mod does not add anything to the Minecraft world,
it can be safely upgraded by simply replacing an older version with a newer
version.

Migrating
---------

If you are coming from the upstream repo, the old config file will transfer the following configs:
- listen_address
- listen_port
- jwm_collector


Configuration
-------------

The mod configuration is located at *config/prometheus_exporter.cfg*.
It will be automatically generated upon server start if it does not already exist.
The default configuration can be seen in the example [examples/prometheus_exporter.cfg](examples/prometheus_exporter.cfg).


Exporter
--------

A sample output from the exporter can be seen in the example [examples/output.txt](examples/output.txt).


Dashboards
----------

Known compatible Grafana dashboards are listed in [dashboards.md].
