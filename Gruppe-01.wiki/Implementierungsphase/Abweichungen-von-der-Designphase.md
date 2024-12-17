## Model

- Notifications für Model -> Client Kommunikation hinzugefügt
- Starke Änderungen im BPMN Diagramm um für Synchronität und Stabilität in der Netzwerkkommunikation zu sorgen
- Neuer State im 'Client' dieser dient dazu, nachdem Würfel das Board in der View über die 'Notifications' zu initialisieren,
  sodass, diese die richtige Startaufstellung anzeigt
- Kreierung abstrakter Klassen um Typsicherheit im State pattern zu gewährleisten
- Implementierung einer Resource Klasse um die Verwaltung von Ressourcen zu vereinfachen
- Implementierung von FehlerCodes um dem Spieler eine Rückmeldung zu geben, wenn ein Fehler auftritt (siehe 'model/src/main/resources/*')
- Grundlagen für multilanguage support hinzugefügt
- Eigenständige Würfel 'Die' Klasse um Zufallsergebnisse zu simulieren, kann auch zum Testen verwendet werden, wenn man würfel ergebnisse vorgibt
- Settings werden nur noch von der View gehandhabt
- Server ist aus einem eigenem Package in die 'View' gewandert, da dieser über die 'View' ähnlich wie in 'Battleship' gestartet wird
- 'BonusCards' haben neben der gleichnamigen Enum nun auch Klassen mit Visitor pattern bekommen, um die Serverseitige einbindung neuer Karten zu vereinfachen
- Pieces werden nun UUIDs zugewiesen, um die Identifikation zu vereinfachen
- Die methoden zur Bestimmung eines Moves wurden komplett überarbeitet (siehe hierzu Flowchart/SequenceDiagramm)

## View

- Keine States sonder Enum MdgaState
- AcousticManager hinzugefügt
- Implementierung eigener Wurfanimationen für jede TSK
- Möglichkeit nicht nur Züge über das Anklicken einer Figur zu realisieren, sondern jetzt auch über das jeweils markierte Feld möglich
- Wechseln zur taktischen Ansicht nur noch über scrollen möglich, dediziert Button entfernt