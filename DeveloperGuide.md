# Developer Guide

Das Projekt ist in drei GitHub Projekten aufgeteilt. 
- Android App
- Common Datenmodell 
- Web-API (Server)

Es wird ein Jenkins Server verwendet für die Continuous Integration der Web-API.


## Android App

Die Karte wird angezeigt mithilfe von Osmdroid. In der App wird "long press" und "single tap" als definierbare Interaktionen mit der Map verwendet. Hierfür ist das *Strategy Pattern* verwendet worden. Der aktive Operator ist in der Singleton Klasse *MapEditorState* gespeichert.

Es existieren aktuell zwei Operatoren:
- RoadEditorOperator (In Arbeit)
- PlaceNearestRoadsOnMapOperator (fertig)


Ein "floating action button" erscheint sobald eine Barriere auf der Karte gesetzt wurde (über PlaceNearestRoadsOnMapOperator). Über diesen wird ein stepper gestartet.

Der stepper entspricht den material design guidelines und wird von Stepstone entwickelt (Apache 2.0 Lic)

Der Stepper wird über *AddObstacleStepperAdapter* aufgebaut. Dort werden die einzelnen Fragmente geladen, welche das Interface *Step* bzw. *BlockingStep* implementieren. 

## Common Datenmodell

Das Common Datenmodell ist hier zu finden: [GitHub Projekt - vincinator/BP-Common](https://github.com/Vincinator/BP-common)

Über Annotationen werden die POJO für Hibernate und Jackson entsprechend konfiguriert. 

### Hibernate Konfiguration
Alle Objekte der gesammten Obstacle Vererbungshierarschie werden in einer einzigen Tabelle gespeichert. 
Hibernate kann sich aus der Tabelle die konkreten Klassen wieder zusammen bauen. 


### Jackson Konfiguration

Für die Kommuniktion zwischen Server und App werden JSON Daten verschickt. Diese sind so konfiguriert, dass die Konkrete Obstacle Klasse in ein JSON-Attribut als string gespeichert wird.

Beispiel:
```JSON
{
    "@class": "bp.common.model.obstacles.Stairs",
    "mName": null,
    "longitude": 49.874978,
    "latitude": 8.655971,
    "id": 1,
    "typeCode": "STAIRS",
    "numberOfStairs": 30,
    "handleAvailable": false,
    "heightOfStairs": 12,
    "name": "haben Treppen namen?"
  }
```

### Konfiguration für dynamsichen Attribut Editor

Über die *@EditableAttribute* Java Annotationen können Attribute der Obstacle POJO makriert werden, damit diese in der APP im Attribut Editor editierbar sind.





