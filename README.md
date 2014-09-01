BukkitLTI
=========

A [Bukkit](http://bukkit.org/) plugin for [MinecraftLTI](https://github.com/instructure/MinecraftLTI), for integrating Bukkit servers with LMS applications.

Links
-----
* [Demo video](http://www.youtube.com/watch?v=cTZgrmnaMko&list=UUSbm2g19jXCOfIe8OusD17w)
* [Tutorial video: Installation of Java, Craftbukkit and BukkitLTI](https://www.youtube.com/watch?v=h4YiyXCjM8U&list=PLVM3VuEHQJSXUWCNuC6dR4AXUVqLPOGmz&index=1)
* [Tutorial video: Configuration of BukkitLTI within Canvas VLE](https://www.youtube.com/watch?v=QfR6g7B_fzM&list=PLVM3VuEHQJSXUWCNuC6dR4AXUVqLPOGmz&index=2)
* [Tutorial video: Assessment in BukkitLTI](https://www.youtube.com/watch?v=FxuMaXqNDbM&list=PLVM3VuEHQJSXUWCNuC6dR4AXUVqLPOGmz&index=3)
* [Tutorial video: Grading in BukkitLTI](https://www.youtube.com/watch?v=lVu3nAX716E&list=PLVM3VuEHQJSXUWCNuC6dR4AXUVqLPOGmz&index=4)
* [Bukkit project](http://dev.bukkit.org/bukkit-plugins/bukkitlti/)

Tutorial videos courtesy of [Joel Mills](https://twitter.com/iLearningUK).

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
