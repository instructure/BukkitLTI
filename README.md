BukkitLTI
=========

An LTI tool plugin for Bukkit.

Links
-----
* [Demo video](https://www.youtube.com/watch?v=FLWm1OdaNrA&feature=youtu.be)
* [Bukkit project](http://dev.bukkit.org/bukkit-plugins/bukkitlti/)

Demo server
-----------

Add the following LTI tool to your LMS:

* Configuration XML: http://minecraft.inseng.net:8133/config.xml
* Consumer key/secret: copy from http://minecraft.inseng.net:8133/consumer

Usage
-----
* Download a JAR from the project releases
* Add to the plugins directory of your Bukkit server
* Restart your server
* Access http://yourserver.example.com:8133/consumer for an LTI key and secret
* Add the LTI tool to your LMS using the key and secret and http://yourserver.example.com:8133/config.xml

Development
------------
- mvn install (or add the Maven project to your IDE)
- copy target/BukkitLTI-0.2.0-jar-with-dependencies.jar into your Bukkit server's plugins directory
