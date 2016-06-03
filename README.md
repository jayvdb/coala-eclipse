# coala for Eclipse

[![Build Status](https://travis-ci.org/coala-analyzer/coala-eclipse.svg?branch=master)](https://travis-ci.org/coala-analyzer/coala-eclipse)

This package offers a plugin for the Eclipse to use the code analysis software [coala](https://github.com/coala-analyzer/coala) inside the IDE.

## Installation
1. Install [coala](https://github.com/coala-analyzer/coala).
2. In Eclipse go to Help -> Install New Software -> Add
	1. In location enter https://github.com/coala-analyzer/coala-eclipse/raw/master/exports
	2. In name enter coala.
	3. Press ok.
3. Uncheck "group items by category". 
4. Select coala.
5. Click next and accept terms.

### Build developmental version
```text
mvn clean verify
```

## Unistall
1. In Eclipse go to Help-> Installation Details -> Installed Software.
2. Select coala.
3. Click uninstall.

