Additional Message Sources
==========================

Prefixed Bundled Message Source
-------------------------------

This class provides a message source containing multiple resource bundles.
The resources bundles are added via the <code>addBasenames</code> method
or dependency injection. Each resource bundle has an assigned prefix
which prefixes each key of all messages. This makes it easier to provide
larger sets of resource bundles by splitting them into multiple files.
Due to the prefix each resource bundle can use shorted message names without
loosing the uniqueness of its key. 

Omitting the prefix and the '#' sign sets the bundle as fallback or default bundle
mapping the key of each message directly onto the keys of the resource bundle.
This is equal to an default namespace. The provided paths after the prefix defines
the path to search for the resource bundle.

```java
PrefixedBundledMessageSource messageSource = new PrefixedBundledMessageSource();

// Adding prefixed resources
messageSource.addBasenames("hello#world");  // Adds "world.properties" with prefix "hello"
messageSource.addBasenames("msg#messages/mymessages");  // "messages/mymessages.properties" with "msg" prefix

// Adding fallback / unprefixed resources
messageSource.addBasenames("other");  // "other.properties" without prefix
messageSource.addBasenames("another");  // "another.properties" without prefix
```

Dependency Injection via Spring:
```xml
<beans:bean id="messageSource" class="org.tpc.resources.PrefixedBundleMessageSource">
  <beans:property name="namePrefix" value="/WEB-INF/messages/"/>
  <beans:property name="basenames">
    <beans:list>
      <beans:value>
        global/global/global,
        lang#global/languages/lang
      </beans:value>
      <beans:value>
        login#login/global/global
      </beans:value>
    </beans:list>
  </beans:property>
</beans:bean>
```
