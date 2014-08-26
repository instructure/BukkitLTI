BukkitLTI
=========

A [Bukkit](http://bukkit.org/) plugin for [MinecraftLTI](https://github.com/instructure/MinecraftLTI), for integrating Bukkit servers with LMS applications.

Links
-----
* [Demo video](http://www.youtube.com/watch?v=cTZgrmnaMko&list=UUSbm2g19jXCOfIe8OusD17w)
* [Bukkit project](http://dev.bukkit.org/bukkit-plugins/bukkitlti/)

Demo server
-----------

Add the following LTI tool to your LMS:

* Configuration XML: http://minecraft.inseng.net:8133/config.xml
* Consumer key/secret: copy from http://minecraft.inseng.net:8133/consumer

Usage
-----
* Download a JAR from the [project releases](https://github.com/instructure/BukkitLTI/releases)
* Add to the plugins directory of your Bukkit server
* Restart your server
* Access http://yourserver.example.com:8133/consumer for an LTI key and secret
* Add the LTI tool to your LMS using the key and secret and http://yourserver.example.com:8133/config.xml

Development
------------
- clone [MinecraftLTI](https://github.com/instructure/MinecraftLTI)
- mvn install
- clone BukkitLTI
- mvn install
- copy the generated jar from your target directory into your Bukkit server's plugins directory
