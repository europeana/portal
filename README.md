![](https://github.com/europeana/portal/blob/master/portal2/src/main/webapp/themes/default/images/europeana-logo-retina-1.png)

# Europeana: catalyst for change in the world of cultural heritage

## What does it do?

Europeana is an internet portal that acts as an interface to millions of books, paintings, films, museum objects and archival records that have been digitised throughout Europe. The portal provides the front-end for accessing these digital objects.

## Full Documentation

See the [Wiki](https://github.com/europeana/portal/wiki) for full documentation, examples, operational details and other information.

The JavaDoc will be generated once the upcoming code overhaul is complete.

## Communication

- [GitHub Issues](https://github.com/europeana/portal/issues)

## Build

To build (requires the [CoreLib](https://github.com/europeana/corelib) dependency):

cp portal2/template-manifest.yml portal2/manifest.yml

Remember to copy properties/test/template-europeana.properties into
properties/test/europeana.properties if you are doing a deploy!



```
$ git clone https://github.com/europeana/portal.git
$ cd portal/
$ mvn clean install -DskipTests
```

Futher details on building can be found on the [Deploy](https://github.com/europeana/portal/wiki/Deploy) page of the wiki (but check the [Setup](https://github.com/europeana/portal/wiki/Setup) first!.

## LICENSE

Copyright 2007-2012 The Europeana Foundation

Licenced under the EUPL, Version 1.1 (the "Licence") and subsequent versions as approved by the European Commission;
You may not use this work except in compliance with the Licence.

You may obtain a copy of the Licence at: [http://joinup.ec.europa.eu/software/page/eupl](http://joinup.ec.europa.eu/software/page/eupl)

Unless required by applicable law or agreed to in writing, software distributed under the Licence is distributed on an "AS IS" basis, without warranties or conditions of any kind, either express or implied. See the Licence for the specific language governing permissions and limitations under the Licence.
