# Präsentation

<details>
<summary markdown="span">07.10.2024</summary>

### Ort, Datum, Zeit

Gebäude 41 Haus 400 Raum 2417, 07.10.2024, 10:00 - 11:15 Uhr

### Teilnehmer

- Bauer Lukas @j23b0233
- Beck Cedric @j23b0826
- Brennförderer Timo @j23b0724
- Feyer Benjamin @j23f0712
- Fleischer Hanno @j23f0779
- Grigencha Daniel @j23g0274
- Koppe Felix @fkoppe

## Was haben wir die vergangene Woche gemacht:

- mit dem Projekt vertraut gemacht
- in das Wiki eingelesen
- in die JMonkey Engine eingelesen und erste Funktionalitäten implementiert

### Aufgabe 07

- Einarbeitung in mockito
    - Verwendung um mock-Objekte zu erstellen, um die Funktionalität von Klassen zu testen (Schwerpunkt: Testen von
      graphischen Benutzeroberflächen)
- Erläuterung des Tests in der Klasse `BattleState` und `ShipMap`

### Aufgabe 08

- Erläuterung der serverseitigen Überprüfung in der `ServerGameLogic`
- mit Bezug auf den erstellten Code:
    - in der Methode `checkMap()`
    - in der Methode `collidesWith()` (gegeben im `BattleShip`)
- clientseitigen Überprüfung im `EditorState`
- genauere Analyse der Lösung von Beck (Anzahl der Schiffe in der Klasse `ServerGameLogic`)

### Aufgabe 09

- Modelle für verbleibende Schiffstypen in der Klasse `SeaSynchronizer` hinzugefügt (gem. Aufgabenstellung)
- Konvertieren der Modelle in `.j3o` Dateien

### Aufgabe 10

- Integration von Hintergrundmusik (eigene Klasse `BackgroundMusic` erstellt, gem. Aufgabenstellung)

### Aufgabe 11

- mit dem Client einen Server erstellen (siehe Konstruktor der Klasse `BattleShipServer`)
- Erläuterung der Methode `connect()`, damit der Client einen Server hostet (siehe Klasse `BattleShipServer`)

### Aufgabe 12

- Überarbeitung der Effekte mit Erläuterung der Funktionalität (siehe `EffectHandler` und Bezug zur Methode
  `createHitEffekt`)
- Sinken der Schiffe mit der Methode `controllUpdate()`

## Anmerkungen vom Betreuer:

- Vermeidung von redundantem Code
- MVC Pattern (Modell unabhängig, von View und Controller)
- zu Aufgabe 10:
    - Features die nicht gefordert waren:
        - Checkbox für Effekte
        - Methode `toggleMusic()` mit den einzelnen States
        - Methode `setVolume()` mit Erläuterung der Funktionalität für die Gesamtlautstärke
        - Methode `changeMusic()` mit Erläuterung der Funktionalität für unterschiedliche Hintergrundmusik abhängig vom
          aktuellen `gameState`
- Verwendung unseres Wikis gem. dem BeispielWiki
- erstes Gruppentreffen Protokoll hat gepasst
- Protokolle immer führen und sorgfältig anlegen
- Rendern von Modellen aufgrund von Erfahrungen des letzten ProgProj

## Was werden wir diese Woche machen:

- Einführungsaufgabe abschließen (bis inkl. Aufgabe 13)
- Lukas Bauer (@j23b0233): Exception von Aufgabe 11 beheben und GUI überarbeiten

## Was hat uns an der Arbeit gehindert:

- Starten des Single Modes (Lukas Bauer @j23b0233 hat versucht seinen eigenen Single Mode zu implementieren)
- Konvertierung der Modelle in `.j3o` Dateien
- Verhältnismäßig wenig Zeit

</details>

<details>
<summary markdown="span">14.10.2024</summary>

### Ort, Datum, Zeit

Gebäude 41 Haus 400 Raum 2417, 07.10.2024, 10:00 - 11:15 Uhr

### Teilnehmer

- Bauer Lukas @j23b0233
- Beck Cedric @j23b0826
- Brennförderer Timo @j23b0724
- Feyer Benjamin @j23f0712
- Fleischer Hanno @j23f0779
- Grigencha Daniel @j23g0274
- Koppe Felix @fkoppe

## Was haben wir die vergangene Woche gemacht:

- Einarbeitungsaufgabe komplettiert

## Was werden wir diese Woche machen:

- Mit der Analysephase beginnen

## Was hat uns an der Arbeit gehindert:

- Aufgabe 13 umfangreicher als erwartet
- Verständnisprobleme der Aufgabenstellung
- DrawIO Einarbeitung

## Präsentation

### Aufgabe 10

- Aufgabenstellung Musik hinzufügen

Klasse `GameSound`
- Methoden: `VolumeSlider()`
- - Implementierung VolumeSlider 

Anmerkung Betreuer: Slider Probleme bei zwei Clients da der Slider auf den Wert des anderen gesetzt wird

### Aufgabe 11

- Aufgabenstellung Server aus Client hosten

Klasse `Networkdialog`
- mit den Änderungen zur Implementierung des features

### Aufgabe 12

- Aufgabenstellung einfügen von Parikeleffekten

Klasse `EffectHandler`
- Methode: `createFire()`
- - Ist für Parikeleffete von Feuer verantwortlich

Klasse `Seasynchronizer`

geänderte bzw. überarbeite funktionen für die Implementierung der Aufgabe
- Methode: `ShipControll()`
- Methode: `handleSinking()`
- Methode: `handleHit()`
- Methode: `hit()`

Anmerkung Betreuer: Schiff wird zu früh entfernt

### Aufgabe 13

- Aufgabenstellung einfügen und Animation von 2D sowie 3D Objekten

Vorstellung folgender Diagramme:

- BPMN Diagramm
- Client Statediagramm
- Server Statediagramm
- Klassendiagramm

Neu hinzugefügte Klasse `Shell` 
- Methode: `move()`
- - ist für die Flugbahn des projectils verantwortlich
- Methode: `updatePosition()`
- - synchronisiert die Shell bei beiden Clients

Anmerkung Betreuer:
Klasse `ShellControll`
- Methode `controllUpdate()`
- - Warum die controllUpdate Funktion bei Feyer Benjamin Feyer Benjamin @j23f0712 nicht überschrieben wurde

- - Antwort: Kein näherer grund

### Vorführung der Anwendungen

Grigencha Daniel @j23g0274
- Exception bei Einschlag einer Shell

Bauer Lukas @j23b0233
- Modell von 2er Schiff zu groß
- Kreative Implementierung der Aufgabe 13

Feyer Benjamin @j23f0712
- Anmerkung Betreuer: Fehlerhafter Aufprall Soundeffekt
- Problem wurde behoben

### Punkte Betreuer

- IntelliJ Shortcut zum aufräumen von Code
- Kommentare zu Code „durchdenken“

## Probleme die bis Freitag 18.10.2024 16:00 beseitigt werden müssen

- Exception Grigencha Daniel @j23g0274
- Musikregler Beck Cedric @j23b0826
- Schiffsanimation Beck Cedric @j23b0826

# Aufstieg in die Analysephase ist genehmigt

</details>

<details>
<summary markdown="span">20.10.2024</summary>

### Ort, Datum, Zeit

Online, 20.10.2024, 22:50 - 23:50 Uhr

### Teilnehmer

- Bauer Lukas @j23b0233
- Beck Cedric @j23b0826
- Brennförderer Timo @j23b0724
- Feyer Benjamin @j23f0712
- Fleischer Hanno @j23f0779
- Grigencha Daniel @j23g0274
- Koppe Felix @fkoppe

## Was haben wir die Vergangene Woche gemacht:

- Kundenwünsche an die Anwendung aufgenommen
- Artefakte für Analysephase erstellt

## Was werden wir die nächste Woche machen:

- Artefakte verbessern
- Artefakte genauer ausführen
- Punkte des Betreuers umsetzen

## Was ist uns schwergefallen:

- Nichts

## Präsentation

- Vorstellung der Artefakte
  - Erweiterte Aufgabenstellung
  - Use Cases
  - Zustandsdiagramm
    - nur Clientzustandsdiagramm
  - Klassendiagramm
  - Testhandbuch
  - Gestaltungsrichtlinien
    - GUI Mock-up
    - Menü Führung
    - Fonts
  - GUI-Skizzen
  
  - Benutzerhandbuch
  - Glossar

## Punkte Betreuer:
- Benutzerhandbuch
  - Extra Dokument mit Quellen des Handbuchs als separate Liste
  - unpassende Benennung des Anhangs -> umbenennen oder auslagern
  - Kapitel Hinweise (Installation von Bibliotheken)
- Erweiterte Aufgabenstellung
  - Bedingungen Sieg und Niederlage
  - Textpassagen ersetzen durch Auflistung
  - Orientieren am Beispiel Dokument
- Use Cases
  - UC-Player-06
    - Vorbedingung: Hat mindestens eine Figur auf dem Feld
    - Nachbedingung: Figur wird hervorgehoben
  - UC-Game-02
    - Hinweis auf mögliche Use Cases (Gewinner hat andere Anzeige als Verlierer)
  - Gestaltungsrichtlinien
    - Sperate Datei für Quellen
    - Quellen für Abbildungen angeben
  - Testhandbuch
    - Ein Use Case kann mehrere test Cases haben
  - Zustandsdiagramm
    - Genaueres Betrachten Zustand Spiel -> eigenes Diagramm für Statepattern im Statepattern
    - Betrachten Verhalten des Spielers
    - Betrachten PowerUp Karten
    - Anfangszustand und Endzustand Symbole beachten
    - Zustandsdiagramm für Menüführung
  - Wiki
    - Artefakte versionieren
    - Umbenennung des Spiels muss überall durchgeführt werden
  - Hinweis:
    - Artefakte, die in der Designphase erstellt werden müssen mit mehr Details versehen werden

</details>

<details>
<summary markdown="span">28.10.2024</summary>

### Ort, Datum, Zeit

Gebäude 41 Haus 400 Raum 2417, 28.10.2024, 10:00 - 10:45 Uhr

### Teilnehmer

- Bauer Lukas @j23b0233
- Beck Cedric @j23b0826
- Brennförderer Timo @j23b0724
- Feyer Benjamin @j23f0712
- Fleischer Hanno @j23f0779
- Grigencha Daniel @j23g0274
- Koppe Felix @fkoppe

## Was haben wir die vergangene Woche gemacht:

- Bestehende Artefakte überarbeitet/ erweitert
- Neue Artefakte hinzugefügt
- Erste Überlegungen für die Designphase angestellt

## Was werden wir diese Woche machen:

- Mit der Designphase beginnen

## Was hat uns an der Arbeit gehindert:

- UseCases führten zu Missverständnissen

## Präsentation

Zielbestimmungen
- Fließtext sind nun Stichpunkte
- Änderungen erläutert

UseCases
- UseCases erweitert/ überarbeitet

TestCases
- UC-Camera 03

Anmerkung Betreuer:
- Mausrad für Zoom Funktion fehlt

Zustandsdiagramme
- Zustände aufgeteilt
- Vorstellung der Artefakte

Anmerkung Betreuer:
- Artefakt des Baumdiagramms soll aus dem Verzeichnis Zustandsdiagramme entfernt werden
- Eine Verlinkung soll vorhanden sein

Klassendiagramme
- Änderung erläutert

Testhandbuch
- TestCases überarbeitet/ hinzugefügt
  - Ceremony
  - Statistik

Anmerkung Betreuer:
- Änderungen immer aktualisieren

AssetListe
- Welche Assets wir benötigen
- Modelle im Low Poly Style

Anmerkung Betreuer:
- Assets sollen in die Gestaltungsrichtlinien

Gestaltungsrichtlinien
- Quellen hinzugefügt

GUI-Skizzen
- Verbindungsfehler
- Statistik Fenster

Benutzerhandbuch
- JRE/ Libraries hinzugefügt
- Spiel verlassen Button
- Award Ceremony

Anmerkung Betreuer:
- JRE 8 oder höher ist Vorgabe und darauf können wir uns beziehen

Glossar
- Überarbeitet
- Rechtschreibung korrigiert

## Punkte Betreuer:
- Für weitere Präsentationen Schwerpunkte setzen auf die man genauer eingeht

## Punkte Tutor:
- Keine

## Punkte Teamleiter:

Frage:

- Müssen in der Designphase UseCases welche, sich aus Zustandsdiagrammen ableiten bearbeitet werden?

Antwort:
- Ja

## Punkte Gruppe:
- Keine

# Aufstieg in die Designphase ist gewährt

</details>

<details>
<summary markdown="span">04.11.2024</summary>

### Ort, Datum, Zeit

Gebäude 41 Haus 400 Raum 2417, 04.11.2024, 10:00 - 10:40 Uhr

### Teilnehmer

- Bauer Lukas @j23b0233
- Beck Cedric @j23b0826
- Brennförderer Timo @j23b0724
- Feyer Benjamin @j23f0712
- Fleischer Hanno @j23f0779
- Grigencha Daniel @j23g0274
- Koppe Felix @fkoppe

## Was haben wir die vergangene Woche gemacht:

- Analysephase nachgearbeitet
- Designphase begonnen

## Was werden wir nächste Woche machen:

- Artefakte überarbeiten/ erneuern
  - Testhandbuch
  - BPMN

## Präsentation:

- Klassendiagramme
- Paketdiagramme
- Zustandsdiagramme
- Asset Liste
- Use Cases
- Benutzerhandbuch

## Punkte vom Betreuer:
- Zustandsdiagramme
  - Server
    - Rolldice soll genauer erklärt werden
  - Client
    - Settings
      - Audio ist mit Video Settings vertauscht
    - ChoosePiece
      - Für bessere Übersichtlichkeit soll SelectPiece verschoben werden
- Klassendiagramm
  - Controller
    - Generalisierungspfeil muss ein Implementierungspfeil sein
  - Use Cases
    - Auf der jeweiligen Seite vermerken, wo sich die Änderungen befinden
  - AssetListe
    - Mit welchen Tools wurden die Modelle erstellt
      - Mit Blender
- Warum haben wir uns für die Architektur entschieden
  - Um unser Spiel vor "Cheats" der Clients zu schützen
- Wiki Titel ändern -> Man don´t get angry 


## Punkte vom Tutor:
- Frage bezüglich einer Message
  - Wurde geklärt

## Fragen vom Teamleiter:
- Bezüglich des Notenscheins fürs Prog Proj
  - Direkt mit Herrn Minas abklären
  
---

### Terminänderung 

- <del>18.11.2024, 10:00, Gebäude 41 Haus 400 Raum 2417</del>
- 19.11.2024, 12:00, Gebäude 41 Haus 400 Raum 2417

</details>

<details>
<summary markdown="span">11.11.2024</summary>

### Ort, Datum, Zeit

Gebäude 41 Haus 400 Raum 2417, 11.11.2024, 10:00 - 11:10 Uhr

### Teilnehmer

- Bauer Lukas @j23b0233
- Beck Cedric @j23b0826
- Brennförderer Timo @j23b0724
- Feyer Benjamin @j23f0712
- Fleischer Hanno @j23f0779
- Grigencha Daniel @j23g0274
- Koppe Felix @fkoppe

## Was haben wir die vergangene Woche gemacht:
- BPMN Diagramm erstellt
- Paketdiagramm erweitert
- Zustandsdiagramme überarbeitet
- Use Cases über die Zustandsübergänge der Zustandsdiagramme geschrieben
- Testhandbuch über die neuen Use Cases

## Was machen wir nächste Woche:
- Mit der Implementierung beginnen

## Was hat uns an der Arbeit gehindert:
- Wechsel des Teamleiters

## Präsentation:

Klassendiagramme:
- Model
  - Unterschiede zur vorherigen Iteration
  - Erläuterung der States welche wir im Client als auch Server Zustandsautomaten benötigen
- View
  - States und ihre Funktion
  - BoardView
  - GameView
  - GuiView

Paketdiagramme:
- Model ist im Paket von Server und Client

Sequenzdiagramme:
- Schwerpunkt der Präsentation
- Erläuterung anhand der Bewegung von Figuren

Flussdiagramme:
- Bewegungen der Figuren

Zustandsdiagramme:
- Änderungen vorgetragen

Use-Cases:
- 120 neue Use Cases welche sich aus den Zustandsdiagrammen abgeleitet haben

Testhandbuch:
- Test Cases für die 120 neuen Use Cases

Asset Liste:
- Map ist nun Modelliert
- Kartenstapel wurde hinzugefügt
- Wartebereiche befinden sich nun auf der Map

### Punkte Betreuer:
- BPMN Diagramm
  - Diagramm soll bis zum Spielende darstellen
  - Client übernimmt zu viele Aufgaben für die Thin Client Architektur
  - BPMN erstellen welches sich auf Würfeln und "ziehen" bezieht

- Flussdiagramme:
  - Move
    - Endzustand soll abgerundet sein
    - Flussdiagramme vereinheitlichen
    - Pfeile sollen "gerade" sein

- MessageList ins Wiki einpflegen

- Klassendiagramme:
  - Hinweis: Durch die Implementierung ergeben sich Änderungen in den jeweiligen Artefakten
  - Getter und Setter können in den Artefakten weggelassen werden

- Sequenzdiagramme:
  - "Die Pfeile" nummerieren
  - TryMove Pfeil von Objekt muss auf den Lebensbalken verweisen
  - Beschreibungen hinzufügen
  - Letzte Rückgabe muss gestrichelt dargestellt werden
  - Die Größe der Lebensbalken passt nicht muss überarbeitet werden

- Allgemeine Punkte:
  - Jeder Kundenkontakt muss dokumentiert werden

### Punkte Tutor:
- Zufrieden mit Artefakten

### Punkte Teamleiter:
- Zufrieden mit Teamleistung

### Punkte Team:
- Keine

# Aufstieg in die Implementierungsphase ist gewährt

</details>

<details>
<summary markdown="span">19.11.2024</summary>

### Ort, Datum, Zeit

Online (Zoom-Meeting), 19.11.2024, 12:00 - 12:40 Uhr

### Teilnehmer

- Bauer Lukas @j23b0233
- Beck Cedric @j23b0826
- Brennförderer Timo @j23b0724
- Feyer Benjamin @j23f0712
- Fleischer Hanno @j23f0779
- Grigencha Daniel @j23g0274
- Koppe Felix @fkoppe

## Was haben wir die vergangene Woche gemacht:
- Artefakte überarbeitet
- BPMN Lobby
- Notification Liste
  - Kommunikation Model - View

## Was machen wir nächste Woche:
- Schwerpunkt Tests
- Model nahezu komplettiert
- View wird weiterentwickelt

## Was hat uns an der Arbeit gehindert:
- Widersprüchliche Artefakte
- Implementierung
  - Verticie Count von 3 Millionen

## Aufgabenverteilung:

- Branches
- Development ist zusammenführung von Model und View
  - Ist Main branch
  - Dev  Test 	- Test
  - Dev Client 	- View
- Model
  - Grigencha, Fleischer
- View
  - Koppe Cedrick
- Test
  - Bauer, Feyer, Brennförderer

## Präsentation:

- BPMN Diagramm
- Erklärung hinzugefügt
  - Turn - Ceremony ergänzt
  - Lobby - neu hinzugefügt
    - Verbindung zwischen Server und Client auf Protokoll ebene
- Notifications
  - Aufführung aller Notifications
- Klassendiagramme
  - Kleinere Änderungen
- Implementierung
  - Model noch nicht fertig
      - Alle Klassen für den Automaten
        - Client hat nur leere Klassen
        - ServerState
    - Abstrakte Klasse Parent
      - Änderungen im Klassendiagramm werden sich ergeben
    - Lobby
      - Transitionen die er machen muss
      - Client wählt Tsk aus - Server muss alle Clients informieren
    - GameState
      - Alle Transitionen
      - Entscheidet welche Transitionen sie macht Intern
    - ServerState machine ist bis zur nächsten Präsentation fertig
    - Game - Klasse die für, dass Spiel zuständig ist
      - Spieler werden mit Farbe gespeichert
      - Statistiken werden gespeichert
  - View
  -	Vorstellung der Anwendung
    - Vertices Anzahl wird optimiert
      - AcousticHandler
      - PlaySound
      - MdgaSound
      - Map.mdga
  - Test werden im Laufe dieser Woche bearbeitet

### Punkte Betreuer:
- Welche Herausforderungen hatten wir speziell in der Implementierungsphase
  - Missverständnisse bei den Artefakten
  - Messages haben gefehlt oder waren fehlerhaft
  - tryMove und Move Logik wurde ausgelagert
- Diagramme, die sich ändern in der jeweiligen Phase einpflegen
  - Klassendiagramme in Implementierungsphase einfügen
- Allgemeine Hinweise:
  - CodeStyle und CheckStyle müssen vor jedem Push durchlaufen
  - Solarlind API für Codeanalyse - Codeoptimierung

### Punkte Tutor:
- Demo
  - Gelb und grün gegen andere Farben austauschen

### Punkte Teamleiter:
- Keine Punkte

### Punkte Team:
- Keine Punkte

</details>

<details>
<summary markdown="span">25.11.2024</summary>

### Ort, Datum, Zeit

Gebäude 41 Haus 400 Raum 2417, 25.11.2024, 10:00 - 10:40 Uhr

### Teilnehmer

- Bauer Lukas @j23b0233
- Beck Cedric @j23b0826
- Brennförderer Timo @j23b0724
- Feyer Benjamin @j23f0712
- Fleischer Hanno @j23f0779
- Grigencha Daniel @j23g0274
- Koppe Felix @fkoppe

Was haben wir die vergangene Woche gemacht:

- Client State und Server State Machine weitergearbeitet
  - 	Server erste Übergänge funktionsfähig
- View
  - 3D Welt Overlay wurde erweitert für 2D Top Down
- Test
  - ClientState Test

Was werden wir die nächste Woche machen:
- Spiel funktionsfähig

Was hat uns an der Arbeit gehindert:

- View
  - GUI über 3D Welt zu rendern
-	Krankheit
-	Prüfungsvorbereitung
-	Versucht Performance zu optimieren

Präsentation:

- Spiel
- GUI
  - Overlay
  - Performance
  - Von 3 Millionen Vertices auf 1 Millionen Vertices
  - Ceremony
- Wenn Server und Client kommunizieren bezüglich Piece
  - Für klare Identifizierung des Pieces
-	Messages wurden mit JavaDocs versehen
-	ServerState
  - Move Methoden sind von der Logik nun im ServerState
    - TryInfieldMove
      - Kurze Erläuterung der Logik
-	View

Punkte Betreuer:

- Performance Probleme ca. 9 FPS
- Wenn sich Artefakte ändern dies sofort anzeigen
- Alle Kundeninteraktionen nachsteuern
- Plenumspräsentation:
  - Humor (Minas als Sonne)
-	Frage zu GUI-Settings - Video
-	afterGameCleanup
  - eine Art Reset
-	InputSynchronizer
  - Interface Analoglistener
  - Kann optimiert werden
-	View
  - MDGA View
    - Viele This Pointer
-	curIndex
  - Für die Bewegung der Figuren
-	Model
  - Player
    - RemoveHandCard
      - Wird benötigt, um nur eine Karte zu entfernen
-	Readme erneuert - aktuell noch von BattleShips

Punkte Tutor:
- Commit hatte einen nicht aussagekräftigen Namen

Punkte Teamleiter:
- Keine Punkte

Punkte Team:
- Fragen zu IP Adressbereich
  - Keine Antwort
- Probleme beim Pushen über 100 Mbyte

</details>

<details>
<summary markdown="span">02.12.2024</summary>

### Ort, Datum, Zeit

Gebäude 41 Haus 400 Raum 2417, 02.12.2024, 10:00 - 10:30 Uhr

### Teilnehmer

- Bauer Lukas @j23b0233
- Beck Cedric @j23b0826
- Brennförderer Timo @j23b0724
- Feyer Benjamin @j23f0712
- Fleischer Hanno @j23f0779
- Grigencha Daniel @j23g0274
- Koppe Felix @fkoppe


### Was haben wir die vergangene Woche gemacht:

- Client in großen Teilen fertiggestellt
- Kommunikation zwischen Server und Client in Lobby
- Designs hinzugefügt
- Performance Probleme gefixt

### Was werden wir nächste Woche machen:

- Spiel fertigstellen

### Was hat uns an der Arbeit gehindert:

- Kommunikationsproblem Server war Fehlerhaft und musste überarbeitet werden
- Serialisierung von Klassen

### Präsentation:

- MainView Server wird gestartet
- Funktionen wie startServer
- Diverse Serverübergänge müssen noch geschrieben werden
- GUI Graphiken wurden eingefügt
- Buttons geben optisches feedback beim darüber hovern
- Videosettings wurden hinzugefügt
- View ist fertig
- Animation, hover Effekte wurden hinzugefügt
- Ceremony wurde graphisch überarbeitet

### Punkte Betreuer:

- Wurden Artefakte überarbeitet?
  - Wurden bearbeitet aber noch nicht in dem Wiki

- Sind die Testklassen fertig?
  - 50 – 60% fertig

- 3 Clients 15 GB Arbeitsspeicher Nutzung
  - Wurde verbessert

- OpenGl ins Benutzerhandbuch aufnehmen
- Artefakte, die sich ändern in die Implementierungsphase übernehmen

  - Betroffene Artefakte:
    - Sequenzdiagramm
    - Zustandsdiagramm
    - Testhandbuch
    - Klassendiagramme müssen aufgesplittet werden

- Sympathie Zustimmung des Publikums gewinnen
- Zeitmanagement beachten

### Punkte Tutor:

- Ordentliche Comit Messages

### Punkte Teamleiter:

- Keine Punkte

### Punkte Team:

- Termin Abschluss Präsentation 11.12.2024 12:30 – 14:00

</details>


<details>
<summary markdown="span">09.12.2024</summary>

### Ort, Datum, Zeit

Gebäude 41 Haus 400 Raum 2417, 09.12.2024, 10:00 - 11:00 Uhr

### Teilnehmer

- Bauer Lukas @j23b0233
- Beck Cedric @j23b0826
- Brennförderer Timo @j23b0724
- Feyer Benjamin @j23f0712
- Fleischer Hanno @j23f0779
- Grigencha Daniel @j23g0274
- Koppe Felix @fkoppe

### Was haben wir gemacht:
- Programmiert im Model
- Server und Client sind fertig
- View ist fertig
- Artefakte wurden erstellt

### Was hat uns an der Arbeit gehindert:

- Schlaf und Bugs finden
- Prüfungsvorbereitung
- Es wurde nicht festgehalten welche Änderungen zur Designphase stattgefunden haben

### Was werden wir nächste Woche machen:

- Letzte Bugs fixen
- Veränderungen zur Designphase ins Wiki eintragen
- Artefakte erstellen

Präsentation:

- Wiki
- Paket Diagramm
  - Geänderte Struktur des Projekts
- Flussdiagramme
  - Bewegen der Figuren wurde grundlegend geändert

- BPMN-Diagramme
  - Ablauf eines Spielstarts in der Lobby
  - Ablauf nach Spielstart zum Herausfinden des startingPlayers
  - Ablauf des Spielens von einem Spieler welche Möglichkeiten er hat
- Klassendiagramme
  - Wurden nicht aktualisiert
- Messages
  - Messages wurden entfernt und hinzugefügt
- Notifikation
  - Keine Änderung
- Code
  - NoPieceState
    - Logik, die uns berechnet welche Pieces wir wann wo benutzen, können
    - Funktion ist genauso wie im State Diagramm implementiert
  - SelectPieceState
    - Move Methoden müssen umgeschrieben werden
    - Wir überprüfen unsere Liste an Movable Pieces, Pieces mit des State home oder infield, dann wird der Target-Index berechnet.

### Vorstellung des Spiels:
-	Die 4 TSK spezifischen "wurf" Animationen
-	Netzwerkfähigkeit
-	Spiel mit 4 Mitspielern


### Punkte Betreuer:

Woher kommt der Branch dev2
- Durch Bugfixes

Wie zweckmäßig ist ein Test Branch
- Für simultanes arbeiten und Verhinderung von Merge Konflikten

Nach Spielen der Swap Card Nullpointer Exception
- Abgleich zwischen Pieces hat nicht funktioniert

4 Clients an einem PC sind nahezu unmöglich
- Performance Probleme sind bekannt
- Lösung ist es über die Konsole zu starten

Warum sind wir auf die neueste Version von Gradle gewechselt
- Aufgrund der Models

Der Unterschied zwischen Dev1 und Dev2
- Klassen und grundlegende Models wurden geändert und wir wollten unseren Fortschritt nicht verlieren

Schwerpunkt:

- Spiel lauffähig machen
- Artefakte nachreichen
- Tests abschließen
- Testhandbuch
- Sämtliche Artefakte, die sich geändert haben müssen in die Implementierungsphase
- Bugs(Plenumspräsentation) Deadline Dienstag 17:00
- Artefakte (Wiki) Termin Deadline Donnerstag:  Dienstschluss
- Worst Case Termin am 07.01.2025

### Punkte Tutor:

-	Wie müssen die Würfel fallen damit man das sieht, was man sehen soll
-	Fake it until you make it
-	Funktionen kommentieren

### Punkte Teamleiter:

-	Keine

### Punkte Team:

-	Keine


</details>