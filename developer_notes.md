
you will need maven3 to build this project

install swtbot 2.10 from the update site: http://download.eclipse.org/technology/swtbot/releases/latest/ 

m2e in eclipse needs to be 1.3+ - I was unable to update and had to redownload the JUNO RCP install to get the latest version

In Eclipse, import existing maven projects, m2 will then prompt to install various conenctors.

The SWTBOT recording plugin (in the eclipse containing the substeps plugin) is useful to quickly capture some test scenarios.  These need tweaking and assertions adding.
