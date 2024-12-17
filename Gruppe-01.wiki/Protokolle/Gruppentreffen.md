## Einarbeitungsphase I

<details>
<summary markdown="span">05.10.2024</summary> 

## Meeting

### Ort, Datum, Zeit

Online Discord, 05.10.2024, 12:10 - 12:40 Uhr

### Teilnehmer

- Bauer Lukas @j23b0233
- Beck Cedric @j23b0826
- Brennförderer Timo @j23b0724
- Feyer Benjamin @j23f0712
- Fleischer Hanno @j23f0779
- Grigencha Daniel @j23g0274

### Ablauf

- Stand Einarbeitungsphase I der Gruppe abgefragt
- Unklarheiten über Code mit der Gruppe beseitigt
- Feststellung wer 1. Präsentation durchführt
- Erinnerung über Erstellung von JavaDocs und Dokumentation
- Vergleich Aufgabe 9 → Texturen der Modelle und Skallierung

### Ergebnisse

- Gruppe01 präsentiert bis Aufgabe 10
- Festlegung wer präsentiert
- Festlegung wer protokolliert

### Schwierigkeiten

- Ein Mitglied hat Schwierigkeiten mit der Abgabe der gewünschten Aufgabe.
- Da unsere Gruppe bereits am Montag präsentiert und die Deadline für Code am Sonntag 16:00 ist bleibt innerhalb der
  ersten Woche wenig Zeit zur Bearbeitung.

### Überarbeiten

- Bauer Lukas überarbeitet Aufgabe 8
- Artefakte hochladen

### Wer Präsentiert Ergebnisse Woche 1

- Fleischer Hanno @j23f0779

### Wer Protokolliert Präsentation Woche 1

- Bauer Lukas @j23b0233

</details>

## Einarbeitungsphase II

<details>
<summary markdown="span">11.10.2024</summary> 

## Meeting

### Ort, Datum, Zeit

Gebäude 2 Haus 200 Wohnebene, 11.10.2024, 10:00 - 10:20 Uhr

### Teilnehmer

- Bauer Lukas @j23b0233
- Beck Cedric @j23b0826
- Brennförderer Timo @j23b0724
- Feyer Benjamin @j23f0712
- Fleischer Hanno @j23f0779
- Grigencha Daniel @j23g0274
- Koppe Felix @fkoppe

### Ablauf

- Stand der Einarbeitungsaufgaben der Gruppe abgefragt
- Unklarheiten über existierenden Code geklärt

### Ergebnisse

- Gruppe-01 befindet sich bei Aufgabe 13
- Jedes Gruppenmitglied erstellt
    - 2 Zustandsdiagramme (für den Client und Server)
    - 1 Klassendiagramm (Update für `:battleship:model`)
    - Update des BPMN-Diagramms
    - vollständige JavaDocs

### Schwierigkeiten

- Implementierung der Aufgabe 13
    - Aufgrund der Komplexität der Aufgabe

### Wer Präsentiert Ergebnisse Woche 2

- Beck Cedric @j23b0826

### Wer Protokolliert Präsentation Woche 2

- Bauer Lukas @j23b0233

</details>

## Analysephase I

<details>
<summary markdown="span">15.10.2024</summary>

### Ort, Datum, Zeit

Gebäude 2 Haus 200 Wohnebene, 15.10.2024, 15:30 - 17:00 Uhr

### Gruppenarbeit Erweiterte Analyse:

- Definitionen
  - Zug: Bewegung einer Figur
  - Turn: alle Aktionen eines Spielers

1. Netzwerkdialog
   - 1 Client hostet Server (Port)
   - Restliche Clients connecten zum Server(IP, Port)
   - Name wählen
   - Start
   - Exit

2. Lobby
   - Farbe/TSK wählen
   - Settings
     - Video
     - Audio
     - Exit
   - als Host: Spiel starten

3. Spiel
   - Beim Start: 1 zufällige Karte, 1 Figur auf A, 3 Figuren auf B
   - Settings
     - Video
     - Audio
     - Exit
   - Auswürfeln der Startreihenfolge -> bei gleicher Augenzahl erneut Würfeln
   - Ausspielen einer Powerkarte vor dem Würfeln
     - Aktiver Spieler würfelt
     - Wenn eine Figur auf A & Figuren in B sind ist mit der Figur auf A zu ziehen, sofern möglich
     - Ansonsten freie Wahl der zu ziehenden Figur und auf ein nicht eigen belegtes Feld
     - Würfeln einer 6 -> Wenn eine Figur in B und kein in A dann ist diese auf A zu ziehen, ist eine
     Figur auf dem A-Feld ist mit dieser zu ziehen, es darf erneut gewürfelt werden, jedoch muss
     mit der Figur gezogen werden, die beim ersten Wurf bewegt wurde
     - Wenn eine Figur auf ein PowerUp- Feld kommt ,erhält der Spieler eine zufällige Sonderkarte
     - Das Haus muss von oben nach unten mit passenden Zügen vom Spielfeld befüllt werden ->
     im Haus kann bewegt/übersprungen werden
     - Wenn keine 6 gewürfelt und nicht gezogen werden kann, endet der Turn
     - Bei Disconnect wird Spieler auf niedrigst möglichen Platz
     - Figuren, Spielfortschritt und Powerkarten anzeigen
     - Hat ein Spieler alle Figuren im Haus, wird seine Platzierung festgelegt und der Spieler
     macht keine Züge mehr
     - Spiel endet, wenn nur noch ein Spieler kein volles Haus hat
     - Wird eine Figur geschlagen, wandert sie zurück in ihr eigenes B-Feld
     - Sonderkarten
       - Turbokarte: verdoppelt die Augenzahl beim nächsten würfeln oder zieht zwei ab
       - Tauschkarte:
       - Schild: Schützt die Figur bis zum nächsten Turn des Spielers vor dem Schmeißen, nicht
       aber dem Platztausch
       - Minas

4. Siegerehrung
   - Die Spieler werden der Gewinnerreihenfolge nach angezeigt

### Schwierigkeiten

-  Es entstehen bei genauerem Nachdenken über Ablauf und Logik viele Fragen, die mit dem Kunden geklärt werden müssen

</details>

<details>
<summary markdown="span">17.10.2024</summary>

### Ort, Datum, Zeit

Gebäude 2 Haus 200 Wohnebene, 17.10.2024, 16:00 - 17:00 Uhr

### Teilnehmer

- Bauer Lukas @j23b0233
- Beck Cedric @j23b0826
- Brennförderer Timo @j23b0724
- Feyer Benjamin @j23f0712
- Fleischer Hanno @j23f0779
- Grigencha Daniel @j23g0274
- Koppe Felix @fkoppe

### Gruppenarbeit:

- Unklarheiten über Spiellogik und Sonderfälle
- Erstellung und Besprechung von Use Cases
- Erstellung des Testhandbuchs

### Schwierigkeiten

- Fragen über Spielablauf und Logik ergeben sich bei der Erstellung von Use Cases

</details>

<details>
<summary markdown="span">18.10.2024</summary>

### Ort, Datum, Zeit

Gebäude 2 Haus 200 Wohnebene, 11.10.2024, 9:30 - 10:00 Uhr

### Teilnehmer

- Bauer Lukas @j23b0233
- Beck Cedric @j23b0826
- Brennförderer Timo @j23b0724
- Feyer Benjamin @j23f0712
- Fleischer Hanno @j23f0779
- Grigencha Daniel @j23g0274
- Koppe Felix @fkoppe

### Was wurde besprochen:

- Stand der Gruppe
- Aufgabenverteilung
- Letzte Unklarheiten über Use Cases

### Ergebnisse

- 116 Use Cases
- Zielbestimmung (Erweiterte Analyse)
- Testhandbuch
- Beschreibung Mensch ärgere dich

### Wer macht was:

Jeder
- Nachdenken über Logik der bestehenden Use Cases

Feyer Benjamin @j23f0712
- Client Statediagramm

Fleischer Hanno @j23f0779
- Klassendiagramm

Brennförderer Timo @j23b0724
- GUI Skizzen

Beck Cedric @j23b0826
- Gestaltungsrichtlinien

### Schwierigkeiten

- Im Vergleich zur Einarbeitungsphase ist nun viel mehr Absprache und Kommunikation notwendig
- Es fallen sehr viele Tätigkeiten an, die auf die Gruppenmitglieder verteilt werden möchten

### Wer Präsentiert:

- Bauer Lukas @j23b0233

### Wer Protokolliert Präsentation:

- Fleischer Hanno @j23f0779

</details>

<details>
<summary markdown="span">19.10.2024</summary>

### Ort, Datum, Zeit
Online, 19.10.2024, 15.00 - 15:30

### Teilnehmer

- Bauer Lukas @j23b0233
- Beck Cedric @j23b0826
- Brennförderer Timo @j23b0724
- Feyer Benjamin @j23f0712
- Fleischer Hanno @j23f0779
- Grigencha Daniel @j23g0274

### Was wollen wir präsentieren:

- Erweiterte Analyse
- Use Cases
- Testhandbuch
- Benutzerhandbuch
- (Gestaltungsrichtlinien)
- Klassendiagramm
- Client State Diagram
- graphische Mock ups
- Analyse (mdga.pdf)

### Was müssen wir noch machen:

- Glossar
- TestCases
- Gestaltungsrichtlinien

### Wer macht was:

- Bauer Lukas @j23b0233
  - Vorbereitung der Präsentation 
  - Feststellung der Vollzähligkeit der zu präsentierenden Inhalte
  
- Beck Cedric @j23b0826
  - Gestaltungsrichtlinien
  
- Brennförderer Timo @j23b0724
  - TestCases
  
- Feyer Benjamin @j23f0712
  - TestCases
  
- Fleischer Hanno @j23f0779
  - TestCases
  
- Grigencha Daniel @j23g0274
  - Benutzerhandbuch
  
- Koppe Felix @fkoppe
  - Liest Korrektur

### Was ist fertig:
- Diagramme
- UseCases

Wer Präsentiert:
- Bauer Lukas @j23b0233

Wer Protokolliert:
- Fleischer Hanno @j23f0779

</details>

## Analysephase II

<details>
<summary markdown="span">21.10.2024</summary> 

### Ort, Datum, Zeit

Gebäude 2 Haus 200 Wohnebene, 21.10.2024, 10:00 - 10:30 Uhr

### Teilnehmer

- Bauer Lukas @j23b0233
- Beck Cedric @j23b0826
- Brennförderer Timo @j23b0724
- Feyer Benjamin @j23f0712
- Fleischer Hanno @j23f0779
- Grigencha Daniel @j23g0274
- Koppe Felix @fkoppe

### Was wurde besprochen:
- Fragen zu Use Cases
- Aufgabenverteilung

### Wer macht was:

- Bauer Lukas @j23b0233
  - Versionierung der Artefakte
- Beck Cedric @j23b0826
  - Gestaltungsrichtlinien
- Brennförderer Timo @j23b0724
  - GUI-Skizzen, Testhandbuch
- Feyer Benjamin @j23f0712
  - Zustandsdiagramme
- Fleischer Hanno @j23f0779
  - Klassendiagramme
- Grigencha Daniel @j23g0274
  - Benutzerhandbuch, Glossar
- Koppe Felix @fkoppe
  - Use Cases

</details>

<details>
<summary markdown="span">23.10.2024</summary> 

### Ort, Datum, Zeit

Gebäude 2 Haus 200 Wohnebene, 23.10.2024, 15:30 - 16:00 Uhr

### Teilnehmer

- Bauer Lukas @j23b0233
- Beck Cedric @j23b0826
- Brennförderer Timo @j23b0724
- Feyer Benjamin @j23f0712
- Fleischer Hanno @j23f0779
- Grigencha Daniel @j23g0274
- Koppe Felix @fkoppe

### Was wurde besprochen:
- Playtesting mit aktuellen Kundenwünschen:
  - Regeln
  - PowerUps
- Stand der Artefakte die überarbeitet wurden

### Wer macht was:
- Bauer Lukas @j23b0233
  - Überarbeitete Artefakte auf Fehler überprüfen
  - Prüfen ob Artefakte nun den Vorgaben des Betreuers entsprechen
- Beck Cedric @j23b0826
  - Gestaltungsrichtlinien
- Brennförderer Timo @j23b0724
  - Use-Cases
- Feyer Benjamin @j23f0712
  - Zustandsdiagramme
- Fleischer Hanno @j23f0779
  - Klassendiagramme
  - Einarbeitung in Gradle
- Grigencha Daniel @j23g0274
  - Quellen hinzufügen
  - Sämtliche Artefakte auf Fehler überprüfen
- Koppe Felix @fkoppe
  - Use Cases überarbeiten/hinzufügen
  
</details>

<details>
<summary markdown="span">24.10.2024</summary> 

### Ort, Datum, Zeit

Gebäude 2 Haus 200 Wohnebene, 24.10.2024, 20:30 - 21:30 Uhr

### Teilnehmer

- Bauer Lukas @j23b0233
- Beck Cedric @j23b0826
- Brennförderer Timo @j23b0724
- Feyer Benjamin @j23f0712
- Fleischer Hanno @j23f0779
- Grigencha Daniel @j23g0274
- Koppe Felix @fkoppe

### Was wurde besprochen:

Fortschritte der Woche:

Grigencha:
- Überarbeitung der UseCases und der Artefakte
- Beifügen von Kommentaren in der UseCase Liste

Feyer:
- Erstellung 18 neuer StateDiagramme

### Frage an Betreuer:

- Liegen jedem TestCase ein UseCase zu Grunde.

### Assets:

Figur, Brett, Environment, Musik, Sounds, Kartenmodell


### Aufgaben:

- UC-Player-08 Glossar bezüglich aus- und anwählen anpassen und UC-Player Korrektur lesen
- UseCases für Stapel und Ablagestapel erstellen + Eintragung Glossar
- GuiMock für Karte auswählen
- Schild Unterdrückung im Glossar definieren
- Glossar Definition (FinalePosition)
- Glossar Definition Bonuskarte und dahingehend Überarbeitung aller Dokumente
- Client kann sich nach Verbindungsabbruch wieder verbinden
- Glossar Erläuterung Verbindungs verlust/Abbruch
- UseCase Verbinden nach aktivem Spiel verlassen
- GUI-Mock für UC-Client-03
- UC-Dialog Probelesen, prüfen
- UC-Media Überarbeiten
- UseCases für Siegerehrung erstellen
- neuer Use Case Bereit drücken ohne eine TSK auswählen System weißt Spieler eine noch offene TSK zu
- UC-Lobby Korrektur lesen
- UC-Server maximal 4 Spieler Connections
- UC-Host 2-4 Spieler um das Spiel starten zu können

</details>

<details>
<summary markdown="span">26.10.2024</summary> 

### Ort, Datum, Zeit

Gebäude 2 Haus 200 Wohnebene, 26.10.2024, 15:30 - 16:00 Uhr

### Teilnehmer

- Bauer Lukas @j23b0233
- Beck Cedric @j23b0826
- Brennförderer Timo @j23b0724
- Feyer Benjamin @j23f0712
- Fleischer Hanno @j23f0779
- Grigencha Daniel @j23g0274
- Koppe Felix @fkoppe

### Was wurde besprochen:

PowerUp Stapel:

- Maximale Anzahl der Karten 40
- Keine Beschränkung der Anzahl der Karten die ein Spieler auf der Hand haben kann

### Aufgabenverteilung

UseCase
- Besprechung der Fortschritte der vergangenen Drei Tage
- Klärung einiger Unklarheiten
  - Spezialfälle in denen 6er mehrfach hintereinander mit PowerUp geworfen werden
- Bereit Button in der Lobby, falls man keine TSK ausgewählt hat

Fortschritt Testhandbuch:

- Abdeckung aller UseCases mit TestCases

Anzeige
- Position und Ausrichtung der Emotes
- Würfeln in extra 3D Ansicht
  - neuer Viewport

Aufgabenverteilung:
- Ablgeich Glossar mit UseCases/ TestCases

Brennförderer Timo @j23b0724
- Mocks Zeichnen

Jeder
- Testhandbuch
  - Überprüfen auf fehlende Tests 


</details>

## Designphase I

<details>
<summary markdown="span">28.10.2024</summary> 

### Ort, Datum, Zeit

Gebäude 2 Haus 200 Wohnebene, 28.10.2024, 19:30 - 20:00 Uhr

### Teilnehmer

- Bauer Lukas @j23b0233
- Brennförderer Timo @j23b0724
- Feyer Benjamin @j23f0712
- Fleischer Hanno @j23f0779
- Grigencha Daniel @j23g0274
- Koppe Felix @fkoppe

### Was wurde besprochen:

Aufgabenverteilung:

Bauer Lukas @j23b0233

  - Zustandsdiagramme
  - Testhandbuch
  - Protokolle
  - Wiki

Beck Cedric @j23b0826
  - AssetListe ind Gestaltungsrichtlinien einfügen
  - Klassendiagramme
  - Paketdiagramme

Brennförderer Timo @j23b0724
  - Testhandbuch
  - GUI-Mocks updaten im Wiki

Feyer Benjamin @j23f0712
  - Zustandsdiagramme: ClientState, Wie kommt man in den End-Zustand?
  - Baumdiagramm (Hierarchie) für Zustandsdiagramme in eine extra Wiki-Seite einarbeiten mit Verbindung zu den Zustandsdiagramme
  - Erläuterung zu den Zustandsdiagrammen besser Struktur des Dokuments (aber Erklärung des Zustandsdiagramms passt)
  - Flussdiagramme
  - Zustandsdiagramme

Fleischer Hanno @j23f0779
  - Klassendiagramme
  - Paketdiagramme
  
Grigencha Daniel @j23g0274
  - Benutzerhandbuch: Zoomen einarbeiten und UC-Camera: Steuerung explizit angeben
  - Benutzerhandbuch: Wiederverbinden und Disconnect einarbeiten
  - Host kann IP nicht eingeben. Erklärung im Benutzerhandbuch.
  - Flussdiagramme
  - Zustandsdiagramme
  
Koppe Felix @fkoppe
  - Klassendiagramme
  - Paketdiagramme


</details>

<details>
<summary markdown="span">30.10.2024</summary> 

### Ort, Datum, Zeit
Geb. 2 Haus 200 WB, 30.10.2024, 13.00 - 14:00

### Teilnehmer

- Bauer Lukas @j23b0233
- Beck Cedric @j23b0826
- Brennförderer Timo @j23b0724
- Feyer Benjamin @j23f0712
- Fleischer Hanno @j23f0779
- Grigencha Daniel @j23g0274
- Koppe Felix @fkoppe

### Was wurde besprochen:

- Allgemeine Punkte:
  -Klassendiagramme haben Priorität

- Mocks
  - Präsentation der Modelle und Farbwahl für die einzelnen Figuren

- Testhandbuch
  - Rechtsschreibprüfung

- Klassendiagramme
  - Präsentation des Klassendiagramms
- Zustandsdiagramme
  - Müssen erneuert werden
  - 
## Aufträge
- Bauer Lukas @j23b0233
  - unverändert
- Beck Cedric @j23b0826
  - unverändert
- Feyer Benjamin @j23f0712
  - Zustandsdiagramme
- Fleischer Hanno @j23f0779
  - Sequenzdiagramme
    - Move
    - (Clone)

## Wer Präsentiert:
- Feyer Benjamin @j23f0712

## Wer Protokolliert:
- Bauer Lukas @j23b0233




</details>

<details>
<summary markdown="span">02.11.2024</summary> 

### Ort, Datum, Zeit

Online, 02.11.2024, 09:00 - 10:15

### Teilnehmer

- Beck Cedric @j23b0826
- Brennförderer Timo @j23b0724
- Feyer Benjamin @j23f0712
- Fleischer Hanno @j23f0779
- Grigencha Daniel @j23g0274

### Was wurde besprochen:

- Aufgabenverteilung
- Aktueller Stand

### Was wurde gemacht:

- Beck Cedric @j23b0826
  - Mocks von graphical representation auch schon erste Ideen für die Map, assest wurden der Liste hinzugefügt Landschaft al sein großes 3D Modell Ummodellierung des eigentlichen Spielfeldes

- Fleischer Hanno @j23f0779
  - Vorstellung Controller class und package diagram sequenzdiagramm für move methode bis Sonntag und Klassendiagramm kombinieren (controller-model) controller diagramm states hinzufügen

- Feyer Benjamin @j23f0712
  - Zustandsdiagramme fehlende Bedingungen Anmerkung zu Automaten (innere Automaten Erklärung Conditionele Pfeile aus dem Unter Automat) für die Implementierung choose Piece mit negierten Abfrage schreiben

- Grigencha Daniel @j23g0274
  - schreiben erklär Text für dass Zustandsdiagramm 

- Bauer Lukas @j23b0233
  - Hochladen Dateien in die Wiki

- Aufgabe für alle
  - To-do-Liste: Weiter durcharbeiten nach Zuteilung

Wer Präsentiert:
- Feyer Benjamin @j23f0712

Wer Protokolliert:
- Bauer Lukas @j23b0233


</details>


## Designphase II

<details>
<summary markdown="span">04.11.2024</summary> 

### Ort, Datum, Zeit

Gebäude 2 Haus 200 Wohnebene, 04.11.2024, 18:30 - 19:10 Uhr

### Teilnehmer

- Bauer Lukas @j23b0233
- Beck Cedric @j23b0826
- Brennförderer Timo @j23b0724
- Feyer Benjamin @j23f0712
- Fleischer Hanno @j23f0779
- Grigencha Daniel @j23g0274
- Koppe Felix @fkoppe

## Was wurde besprochen:

Besprechung neuer State für Animationen

Aufgabenverteilung:

Beck, Koppe
- BPMN für groben überblick 

Fleischer
- Sequenzdiagramm


</details>

<details>
<summary markdown="span">06.11.2024</summary> 

### Ort, Datum, Zeit

Gebäude 2 Haus 200 Wohnebene, 06.11.2024, 13:30 - 15:10 Uhr

### Teilnehmer

- Bauer Lukas @j23b0233
- Beck Cedric @j23b0826
- Brennförderer Timo @j23b0724
- Feyer Benjamin @j23f0712
- Fleischer Hanno @j23f0779
- Grigencha Daniel @j23g0274
- Koppe Felix @fkoppe

Was wurde besprochen:



Wir verwenden die ThinClients Architektur:

- ThinClients -> Der Server triggert die Zustandsübergänge der Clients
- Clients sind nichtmehr in der Lage Spiellogik zu verwenden
- Server/ClientGameLogic vom Controller ins Model
- Pakete festgelegt


- Ordnerstruktur

mdga

|-------model

|     	|--------->server

|	|-------->client

|	|-------->game

|	|-------->messages

|	|-------->notifications

|-------client

|-------cerver

|-------util


## Aufgabenverteilung: (w=wichtig)

- Sequenzdiagramm Fleischer, Beck, Koppe w
- Zustandsdiagramm Feyer
- BPNM-Diagramm Feyer, Koppe
- Klassendiagramm Fleischer, Beck, Koppe w
- Paketdiagramm Fleischer
- USE-Cases für Zustandsübergänge Feyer, Bauer w
- Testhandbuch Brennförder, Feyer
- Flussdiagramm Fleischer, Beck, Koppe w
- Assets Beck

## Absichten:
- Freitag 0900 Gruppentreffen, nach Möglichkeit alle Aufgaben fertig
- Neuer Gruppenleiter Fleischer Hanno @j23f0779

</details>

<details>
<summary markdown="span">07.11.2024</summary> 

### Ort, Datum, Zeit

Gebäude 2 Haus 200 Wohnebene, 07.11.2024, 15:30 - 16:00 Uhr

### Teilnehmer

- Bauer Lukas @j23b0233
- Beck Cedric @j23b0826
- Brennförderer Timo @j23b0724
- Feyer Benjamin @j23f0712
- Fleischer Hanno @j23f0779
- Grigencha Daniel @j23g0274
- Koppe Felix @fkoppe

## Was wurde besprochen

- ServerGameLogic
- Aufgabenverteilung
- Aktueller Stand
- BPMN
  - Messages und Umsetzung
- Zustandsdiagramm
  - Diagramme im Allgemeinen und daraus resultierende UseCases

</details>

<details>

<summary markdown="span">08.11.2024</summary> 

### Ort, Datum, Zeit

Gebäude 2 Haus 200 Wohnebene, 08.11.2024, 10:00 - 10:45 Uhr

### Teilnehmer

- Bauer Lukas @j23b0233
- Beck Cedric @j23b0826
- Brennförderer Timo @j23b0724
- Feyer Benjamin @j23f0712
- Fleischer Hanno @j23f0779
- Grigencha Daniel @j23g0274
- Koppe Felix @fkoppe

## Was wurde besprochen:

- Aktueller Stand
- Aufgabenverteilung
- Bauer Lukas @j23b0233
  - UseCases, Wiki
- Beck Cedric @j23b0826
  - Assets überarbeiten
- Brennförderer Timo @j23b0724
  - Testhandbuch
- Feyer Benjamin @j23f0712
  - UseCases, Testhandbuch
- Fleischer Hanno @j23f0779
  - Diagramme
    - Sequenzdiagramme
    - Klassendiagramme
- Grigencha Daniel @j23g0274
  - UseCases
- Koppe Felix @fkoppe
  - Diagramme
    - Klassendiagramme
    - Sequenzdiagramme
    - BPMN

## Wer Präsentiert:
- Grigencha Daniel @j23g0274

## Wer Protokolliert:
- Bauer Lukas @j23b0233

</details>

<details>

<summary markdown="span">10.11.2024</summary> 

### Ort, Datum, Zeit

Gebäude 2 Haus 200 Wohnebene, 10.11.2024, 10:00 - 10:20 Uhr

### Teilnehmer

- Brennförderer Timo @j23b0724
- Feyer Benjamin @j23f0712
- Fleischer Hanno @j23f0779
- Grigencha Daniel @j23g0274
- Koppe Felix @fkoppe

## Erledigung letzte Woche:
- Zustandsdiagramme überarbeitet für Thin Client
- Testhandbuch
  - Tests für Zustandsübergänge
- Messages: Dokument für Erklärung der Messages
  - Abklärung von Messages: ContinueGame und ForceRestartGame
  - auch Hochladen
- Klassendiagramme überarbeitet
  - States und Messages in das Modell eingearbeitet
  - neue Methoden
  - ein großes für eine Übersicht und aufgeteilt in mehrere Diagramme
- Sequenzdiagramme erarbeitet
  - infield-move und gethome-move
- Flussdiagramme erarbeitet
- UseCases für Zustandsübergänge erarbeitet
  
## Aufgabenverteilung:
- Koppe: Messages überarbeiten
- Daniel / Luk: Einpflegen der UC in ein Word Dokument
- Timo / Ben: Testhandbuch
- Jeder: UseCases - Kurzbeschreibung mit Messages für ClientState

## Präsentation: 
- Grigencha Daniel @j23g0274
  
## Protokoll: Lukas Bauer
- Bauer Lukas @j23b0233

</details>

## Implementierungsphase I

<details>

<summary markdown="span">11.11.2024</summary> 

### Ort, Datum, Zeit

Gebäude 2 Haus 200 Wohnebene, 11.11.2024, 19:00 - 20:00 Uhr

### Teilnehmer

- Bauer Lukas @j23b0233
- Beck Cedric @j23b0826
- Brennförderer Timo @j23b0724
- Feyer Benjamin @j23f0712
- Fleischer Hanno @j23f0779
- Grigencha Daniel @j23g0274
- Koppe Felix @fkoppe

Was wurde besprochen:

Aufgabenverteilung

- Messages in ein eigenes Diagramm  *Bauer*
- Klassendiagramm ohne Whitespaces	*Brennförderer*
- sauber Pfeile im Klassendiagramm	*Brennförderer*
- Klassendiagramm aufsplitten in kleinere (für Präsentation)
- Klassendiagramm uploaden	*Bauer*
- Klassendiagramm erweitern mit Methoden / Attribute  
- kurze Erklärung für das Klassendigramm	*Grigencha*
- Paketdiagramm benennung	*Bauer*
- Paketdiagramm alle Pfeile + saubere Pfeile	*Brennförderer*
- kurze Erklärung für das Paketdiagramm	*Fleischer*
- Sequenzdiagramme alle Pfeile einfügen	*Beck*
- Sequenzdiagramme erweitern (Methodenrückgabe: tryHomeMove)	*Beck*
- Sequenzdiagramm sauber ziehen	*Beck*
- Sequenzdiagramm: Pfeile nummerieren	*Beck*
- Sequenzdiagramm: Rückgabe gestrichelt	*Beck*
- Sequenzdiagramm: Pfeile auf Lebensbalken + größe des Lebensbalken	*Beck*
- kurze Erklärung für das Sequenzdiagramm	*Beck*
- Flussdiagramm saubere Pfeile	*Brennförderer*
- Flussdiagramm sinnvolle Benennung	*Bauer*
- Flussdiagramme vereinheitlichen	*Bauer*
- kurze Erklärung für das Flussdiagramm	*Bauer*
- Endzustände einfügen (Zustandsdiagramme)	*Feyer*
- Zustandsdiagramme geupdated hochladen	*Feyer*
- Erläuterung für das Zustandsdiagramm	*Grigencha*
- BPMN Diagramm ausweiten (bis Spielende)	*Koppe*
- BPMN Diagramm Erläuterung	*Koppe*
- UseCases-Dokument hochladen	*Bauer*
- Wiki Zustandsdiagramme aktualliesieren	*Bauer*
- Zustandsautomat (Model: Client + Server)	*Grigencha*
- Model / Game	*Fleischer*
- View	*Koppe / Beck*
- Test	*Feyer / Brennförderer / Bauer*

###  Besprechung der Implementierung der Klassen:
- Client State
- Server State

### Besprechung des BPMN-Diagramms für den gesamten Spielablauf

</details>

<details>

<summary markdown="span">18.11.2024</summary> 

### Ort, Datum, Zeit

Gebäude 2 Haus 200 Wohnebene, 18.11.2024, 10:00 - 10:30 Uhr

### Teilnehmer

- Bauer Lukas @j23b0233
- Beck Cedric @j23b0826
- Feyer Benjamin @j23f0712
- Grigencha Daniel @j23g0274
- Koppe Felix @fkoppe

Was wurde besprochen:

- Map muss optimiert werden
- Sounds wurden hinzugefügt
- Zustandsautomat ist "fast fertig"
- Klassendiagramme müssen angepasst werden
- Welche Artefakte wurden überarbeitet


</details>

## Implementierungsphase II

<details>

<summary markdown="span">24.11.2024</summary> 

### Ort, Datum, Zeit

Gebäude 2 Haus 200 Wohnebene, 24.11.2024, 22:00 - 22:35 Uhr

### Teilnehmer

- Bauer Lukas @j23b0233
- Beck Cedric @j23b0826
- Feyer Benjamin @j23f0712
- Fleischer Hanno @j23f0779
- Grigencha Daniel @j23g0274

Was haben wir gemacht:

Model

- State Automat Server Client

- Model angepasst aufgrund der Kommunikation von Pieces

View

- Zweites GUI-Element eingefügt - Anzeige Menüführung

Tests

- Vorbereitungen Getter/Setter


### Wer Präsentiert:

- Fleischer Hanno @j23f0779


### Wer Protokolliert:

- Bauer Lukas @j23b0233

</details>

<details>
<summary markdown="span">29.11.2024</summary> 

### Ort, Datum, Zeit

Gebäude 2 Haus 200 Wohnebene, 24.10.2024, 10:00 - 10:30 Uhr

### Teilnehmer

- Bauer Lukas @j23b0233
- Beck Cedric @j23b0826
- Brennförderer Timo @j23b0724
- Fleischer Hanno @j23f0779
- Grigencha Daniel @j23g0274
- Koppe Felix @fkoppe

### Was wurde besprochen:
- Model -> aktueller Stand
- View -> aktueller Stand
- Server -> aktueller Stand
- Tests -> aktueller Stand
- Checkstyle!!
- Terminfindung Präsentation

### Wer Präsentiert:

- Fleischer Hanno @j23f0779


### Wer Protokolliert:

- Bauer Lukas @j23b0233

</details>


## Implementierungsphase III

<details>
<summary markdown="span">08.12.2024</summary> 

### Ort, Datum, Zeit

Gebäude 2 Haus 200 Wohnebene, 24.10.2024, 10:00 - 10:30 Uhr

### Teilnehmer

- Bauer Lukas @j23b0233
- Beck Cedric @j23b0826
- Brennförderer Timo @j23b0724
- Fleischer Hanno @j23f0779
- Grigencha Daniel @j23g0274
- Koppe Felix @fkoppe

Was wurde besprochen:
- Aktueller Stand des Projekts
- Bugs
- Artefakte

### Wer Präsentiert:

- Grigencha Daniel @j23g0274

### Wer Protokolliert:

- Bauer Lukas @j23b0233

</details>