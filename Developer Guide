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





