To create a production version of the project, run the following maven command:

mvn clean gwt:compile install -P gwt-prod

To simplify development, there are several options to combine. 

First, be sure to clean the project, if it has been compiled in production mode before.

Afterwards, you may want to start mongodb and the GWT codeserver:

mvn gwt:compile install embedmongo:start gwt:run-codeserver -P gwt-dev

Once the codeserver has been started, follow the codeserver instructions
and add bookmarklets to Chrome (By default, visit http://localhost:9876/).

Once done, leave the codeserver running and start jetty. You must use 
the gwt-dev profile, and can additionally define two other profiles.

"import" will tell maalr to import initial data into the database, and
"dict-ru" or "dict-en-min" will select the configuration to use.

Have a look at the profiles-section of the pom.

Examples: 

Run jetty without any imports, using the default dictionary:
mvn jetty:run -P gwt-dev

Run jetty with the english dictionary, importing data during startup
mvn jetty:run -P gwt-dev import dict-en-min

Once jetty has been started, you can modify JSPs without the need of a
server restart. You can also modify the GWT code, and use the devmode bookmarklets
to recompile the code on the fly, without restarting the server.
