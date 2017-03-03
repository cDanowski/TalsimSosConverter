# TalsimSosConverter
A Converter that feeds a transactional SOS instance with information from the TALSIM project

## authorization.properties

As the TalsimSosConverter requires **authorization** to execute *InsertSensor* and *InsertObservation* requests of a transactional SOS instance, the access token that is sent with each request has to be configured. Within the repository the file **src/main/resources/authorization.properties** comprises the property "pathToTokenFile" representing the path to a local properties file that contains the actual *authorization token value* as property "token".

E.g., the content of **src/main/resources/authorization.properties** might look like:

```
pathToTokenFile=<path>/token.properties
```

It references a **token.properties** file, whose content MUST contain the following property:

```
token=<tokenValue>
```

The property *token* stores the token that is sent as value of request header "Authorization". 