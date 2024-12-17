
# Analysephase I

<details>
<summary markdown="span">14.10.2024</summary>

### Ort, Datum, Zeit

Gebäude 2 Haus 200 Raum Wohnebene, 14.10.2024, 19:30 - 20:30 Uhr

### Teilnehmer

- Bauer Lukas @j23b0233
- Beck Cedric @j23b0826
- Brennförderer Timo @j23b0724
- Feyer Benjamin @j23f0712
- Fleischer Hanno @j23f0779
- Grigencha Daniel @j23g0274
- Koppe Felix @fkoppe


- Tutor: Krusch

## Was wurde besprochen:

- Spielregeln
- Wünsche und Anforderungen an das Spiel
- Use Cases

## Wünsche:

Allgemein:

- Möglichkeit selbst zu hosten wie in der Einarbeitungsaufgabe
- Bei der Erstellung eines Spiels soll zu Beginn die Farbe der Figuren der jeweiligen Spieler gewählt werden können
- Jeder Spieler soll die Möglichkeit haben, zu Beginn einer Partie seinen Namen ändern bzw. eingeben zu können


Gameplay:

- Zu Beginn einer Partie soll jeder Spieler mit einer Figur bei dem jeweiligen Startplatz starten
- Spiel läuft, solange es noch mindestens zwei Spieler nicht alle Figuren im "Haus" haben

GUI:

- Keine automatisch rotierende Kamera
- Zeigt gegnerische Spieler an mit Positionen und Zusatzinformationen
- Aufleuchten der eigenen Figuren, mit denen interagiert werden kann
- Figuren sollen in einer 3D Umgebung bewegt werden
- Gibt dem jeweiligen Spieler Nachrichten, ob er am Zug ist bzw. wie er das Spielgeschehen manipulieren kann

Spielfeld:

- Das Spielfeld soll ein "Spielfeld" auf einem Tisch sein
- (Optional) Mit einer Skybox
- Soll für vertikale Varianz Objecte wie Häuser, Bäume etc. enthalten

Power Ups:

- Power Ups sollen auch negative Effekte haben "Verdoppel die gewürfelte Augenzahl oder geh 5 Felder zurück"

Game Over:

- Am Ende soll ein Bildschirm mit Siegerehrung angezeigt werden, welcher des Weiteren Statistiken wie z.B. die zurückgelegte Strecke oder eingesetzte Power Ups anzeigt


Sounddesign (Optional):

- "Eigene Aufnahmen sollen präferiert verwendet werden"

Interaktion (Optional):

- Es soll für Spieler möglich sein, miteinander in Form von "Emotes" zu interagieren

Würfelmechanik (Optional):

- 3D Animation beim Würfeln

</details>

<details>
<summary markdown="span">16.10.2024</summary>

### Ort, Datum, Zeit

Gebäude 2 Haus 200 Raum Wohnebene, 16.10.2024, 20:00 - 20:45 Uhr

### Teilnehmer

- Bauer Lukas @j23b0233
- Beck Cedric @j23b0826
- Feyer Benjamin @j23f0712
- Fleischer Hanno @j23f0779
- Grigencha Daniel @j23g0274


- Tutor: Krusch

## Was wurde besprochen:

- Fragen über Spiellogik und Sonderfälle

## Entscheidungen des Kunden:

Unterscheidung der Spielfiguren:

- Es gibt vier Teams, bei denen die kreative Gestaltung der Figuren frei ist.

Verhalten auf Bonusfeldern:

- Wenn eine eigene Spielfigur auf einem Bonusfeld steht, kann sie dort stehen bleiben und muss nicht zwingend weggezogen werden, um Platz für andere eigene Figuren zu machen.

Netzwerkport für das Spiel:

- Der Port soll im Netzwerk-Dialog änderbar sein, aber es soll einen Standardport in der Konfigurationsdatei geben.

Spielstart bei nicht vollständiger Lobby:

- Das Spiel kann nur gestartet werden, wenn alle Spieler in der Lobby ihre Bereitschaft gemeldet haben. Ein Bereit-Button sendet die Bestätigung an den Server.

Verhalten bei Spielabbruch oder Verbindungsverlust:

- Wenn der Host das Spiel beendet, ein Spieler das Spiel verlässt oder die Verbindung verloren geht, soll der Spieler eine Nachricht erhalten und in den Netzwerk-Dialog zurückgeführt werden.

Verhalten des Hosts nach Spielende:

- Nach Spielende soll die IP-Adresse zwischengespeichert werden, damit der Host in die Lobby zurückkehren kann. Der Server bleibt bestehen.

Bewegung bei Blockaden durch eigene Figuren:

- Wenn man eine Sechs gewürfelt und eine Figur bewegt hat, aber die gleiche Figur beim nächsten Wurf nicht bewegen kann, weil sie durch eine andere eigene Figur blockiert wird, soll man eine andere Figur bewegen dürfen. Pro Zug kann jedoch nur eine Figur bewegt werden.

Regeln für das "Ins Haus"-Kommen:

- Spielfiguren dürfen sich im Haus bewegen, dürfen dabei aber nicht übersprungen werden.

PowerUps – zufälliges oder gewähltes Tauschen:

- PowerUps sollen vom Spieler ausgewählt werden können, nicht zufällig getauscht werden.

Fortschrittsanzeige:

- Die Fortschrittsanzeige soll für alle vier Spieler sichtbar sein und sich automatisch nach dem Fortschritt der Spieler sortieren.

Kamerabewegung und Zoom:

- Es soll möglich sein, die Kamera zu rotieren und zu zoomen.

Optional: Ein Tastendruck soll eine weit heraus gezoomte Top-Down-Ansicht ermöglichen.

Begrenzte PowerUps:

- Ausgespielte PowerUps sollen zurück in einen "Kartenstapel" kommen und erneut verwendet werden können.

PowerUp-Verteilung (Win/Lose-Verhältnis):

- Bei PowerUps soll eine Verteilung von 80% Positive Eigenschaft und 20% Negative Eigenschaft erfolgen.
- Beispiel: Eine Turbokarte soll in 60% der Fälle den Effekt haben die gewürfelte Augensumme zu verdoppeln,
in 20% der fällen den Effekt die gewürfelte Augensumme nicht zu verändern und in 20% der fällen den Effekt die gewürfelte Augensumme auf 0 zu setzen.

## Optionale Anforderungen:

Statistiken in der Siegerehrung

- Es sollen nach Spielende in der Siegerehrung Statistiken angezeigt werden können.
- zum Beispiel:
  - Spieler der am häufigsten Power Ups eingesetzt hat
  - Spieler dessen Figuren am häufigsten geschlagen wurden
  

Wiederaufnahme nach Verbindungsabbruch

- Es soll möglich sein, nach einem Verbindungsabbruch die Verbindung wiederherzustellen.

</details>

<details>
<summary markdown="span">18.10.2024</summary>

### Ort, Datum, Zeit

Gebäude 2 Haus 200 Raum Wohnebene, 18.10.2024, 9:00 - 9:30 Uhr

### Teilnehmer

- Bauer Lukas @j23b0233
- Beck Cedric @j23b0826
- Brennförderer Timo @j23b0724
- Feyer Benjamin @j23f0712
- Fleischer Hanno @j23f0779
- Grigencha Daniel @j23g0274
- Koppe Felix @fkoppe


- Tutor: Krusch

## Was wurde besprochen:

- Fragen des Tutoren über bestehende Use Cases beseitigt
- Unklarheiten über Spielablauf geklärt
- Fragen an Kunden über Spiellogik

## Fragen:

Use Cases über Schild PowerUp
- Fragen zu Sonderfällen
- Fragen über Einsatzmöglichkeiten

</details>

# Analysephase II

<details>
<summary markdown="span">24.10.2024</summary>

### Ort, Datum, Zeit

Gebäude 2 Haus 200 Raum Wohnebene, 24.10.2024, 20:00 - 20:30 Uhr

### Teilnehmer

- Bauer Lukas @j23b0233
- Beck Cedric @j23b0826
- Brennförderer Timo @j23b0724
- Feyer Benjamin @j23f0712
- Fleischer Hanno @j23f0779
- Grigencha Daniel @j23g0274
- Koppe Felix @fkoppe

- Tutor: Krusch

## Was wurde besprochen:

Fragen an Tutor:

Frage:
 
- Turbokarte gespielt und 6 gewürfelt Figur muss Wartebereich verlassen?

Antwort:

- Kein Effekt wird angewandt

Frage bezüglich StatePattern:

Antwort:

Erklärung der neue kreierten Diagramme gegenüber dem Kunden.

Benennung der einzelnen Diagramme direkt im Diagramm oben links Erklärung anhand des StateDiagramms RollDice


</details>

# Designphase I

<details>
<summary markdown="span">29.10.2024</summary>

### Ort, Datum, Zeit

Gebäude 2 Haus 200 Raum Wohnebene, 29.10.2024, 20:00 - 20:15 Uhr

### Teilnehmer

- Bauer Lukas @j23b0233
- Beck Cedric @j23b0826
- Brennförderer Timo @j23b0724
- Feyer Benjamin @j23f0712
- Fleischer Hanno @j23f0779
- Grigencha Daniel @j23g0274
- Koppe Felix @fkoppe

- Tutor: Krusch

## Was wurde besprochen:

- Fragen zu Graphischer Vorstellung

Antwort: Nach eigenem Ermessen

</details>

<details>
<summary markdown="span">31.10.2024</summary>

### Ort, Datum, Zeit

Gebäude 2 Haus 200 Raum Wohnebene, 31.10.2024, 19:00 - 20:15 Uhr

### Teilnehmer

- Bauer Lukas @j23b0233
- Beck Cedric @j23b0826
- Brennförderer Timo @j23b0724
- Feyer Benjamin @j23f0712
- Fleischer Hanno @j23f0779
- Grigencha Daniel @j23g0274
- Koppe Felix @fkoppe

- Tutor: Krusch

## Was wurde besprochen:

- Fragen zur Groben Struktur

Antwort: Grobe Strukturidee wurde vorgestellt. Kunde hat nochmal auf MVC-Paradigma hingewiesen

</details>

<details>
<summary markdown="span">01.11.2024</summary>

### Ort, Datum, Zeit

Gebäude 2 Haus 200 Raum Wohnebene, 01.11.2024, 21:00 - 21:15 Uhr

### Teilnehmer

- Bauer Lukas @j23b0233
- Beck Cedric @j23b0826
- Brennförderer Timo @j23b0724
- Feyer Benjamin @j23f0712
- Fleischer Hanno @j23f0779
- Grigencha Daniel @j23g0274
- Koppe Felix @fkoppe

- Tutor: Krusch

## Was wurde besprochen:

- Welche Funktionen oder Spielregeln sollten unbedingt in der digitalen Version enthalten sein?
- Gibt es bekannte Abweichungen oder Varianten der Spielregeln, die berücksichtigt werden müssen?
- Welche Zielgruppe sollte das Spiel ansprechen (z. B. Kinder, Erwachsene, Gelegenheitsspieler)?

Antwort: Sofort eine Figur auf dem Startfeld; Zielgruppe & Vibe: Mario Party

</details>


# Designphase II

<details>
<summary markdown="span">05.11.2024</summary>

### Ort, Datum, Zeit

Gebäude 2 Haus 200 Raum Wohnebene, 05.11.2024, 22:00 - 22:30 Uhr

### Teilnehmer

- Bauer Lukas @j23b0233
- Beck Cedric @j23b0826
- Brennförderer Timo @j23b0724
- Feyer Benjamin @j23f0712
- Fleischer Hanno @j23f0779
- Grigencha Daniel @j23g0274
- Koppe Felix @fkoppe

- Tutor: Krusch

## Was wurde besprochen:

- Wie detailliert sollte das Design der Spieloberfläche sein? (z. B. minimalistisch oder realistisch?)
- Soll das Spiel 2D oder 3D dargestellt werden?
- Welche Farbschemata und Animationen wären für ein solches Spiel passend?

Antwort: Spiel in 3D; GUI eher simpel und aufgeräumt, zusätzlich Infos als overlay; Farben nach eigenem Ermessen

</details>


<details>
<summary markdown="span">06.11.2024</summary>

### Ort, Datum, Zeit

Gebäude 2 Haus 200 Raum Wohnebene, 06.11.2024, 17:00 - 17:15 Uhr

### Teilnehmer

- Bauer Lukas @j23b0233
- Beck Cedric @j23b0826
- Brennförderer Timo @j23b0724
- Feyer Benjamin @j23f0712
- Fleischer Hanno @j23f0779
- Grigencha Daniel @j23g0274
- Koppe Felix @fkoppe

- Tutor: Krusch

## Was wurde besprochen:

- Wie sollten die Bewegungen der Spielfiguren umgesetzt werden? (z. B. manuell oder automatisiert durch einen Algorithmus?)
- Welche Optionen sollten die Spieler haben, z. B. manuelle Würfel oder automatisches Würfeln?
- Wie kann ich sicherstellen, dass die Regeln korrekt implementiert werden? Gibt es Tricks, um Regeln zu testen?
- Soll das Spiel mit verschiedenen Spielerzahlen funktionieren (z. B. 2 bis 4 Spieler)?

Antwort: Eigenes Ermessen, aber versucht es doch dem Spieler möglichst leicht zu machen und pro aktion nicht zu viele Inputs zu fordern.

</details>


<details>


<summary markdown="span">09.11.2024</summary>

### Ort, Datum, Zeit

Gebäude 2 Haus 200 Raum Wohnebene, 09.11.2024, 19:40 - 20:00 Uhr

### Teilnehmer

- Bauer Lukas @j23b0233
- Beck Cedric @j23b0826
- Brennförderer Timo @j23b0724
- Feyer Benjamin @j23f0712
- Fleischer Hanno @j23f0779
- Grigencha Daniel @j23g0274
- Koppe Felix @fkoppe

- Tutor: Krusch

## Was wurde besprochen:

- Welche Interaktionen sollten einfach und intuitiv sein? (z. B. das Ziehen einer Spielfigur?)
- Welche Rückmeldungen sollte das Spiel den Spielern geben (z. B. visuelle oder akustische Hinweise)?

Antwort Eigenes Ermessen anhand der bisherigen Vorgaben

</details>


# Implementierungsphase I

<details>

<summary markdown="span">12.11.2024</summary>

### Ort, Datum, Zeit

Gebäude 2 Haus 200 Raum Wohnebene, 12.11.2024, 18:20 - 18:30 Uhr

### Teilnehmer

- Bauer Lukas @j23b0233
- Beck Cedric @j23b0826
- Brennförderer Timo @j23b0724
- Feyer Benjamin @j23f0712
- Fleischer Hanno @j23f0779
- Grigencha Daniel @j23g0274
- Koppe Felix @fkoppe

- Tutor: Krusch

## Was wurde besprochen:

- Soll das Spiel erweiterbar sein, z. B. mit zusätzlichen Regeln, Spielfeldgrößen oder Designs?

Antwort: Nein

</details>

<details>
<summary markdown="span">14.11.2024</summary>

### Ort, Datum, Zeit

Gebäude 2 Haus 200 Raum Wohnebene, 14.11.2024, 19:50 - 20:10 Uhr

### Teilnehmer

- Bauer Lukas @j23b0233
- Beck Cedric @j23b0826
- Brennförderer Timo @j23b0724
- Feyer Benjamin @j23f0712
- Fleischer Hanno @j23f0779
- Grigencha Daniel @j23g0274
- Koppe Felix @fkoppe

- Tutor: Krusch

## Was wurde besprochen:

- Soll es ein Tutorial geben, das die Spielregeln erklärt?

Antwort: Nein

</details>


<details>
<summary markdown="span">16.11.2024</summary>

### Ort, Datum, Zeit

Gebäude 2 Haus 200 Raum Wohnebene, 16.11.2024, 17:20 - 17:30 Uhr

### Teilnehmer

- Bauer Lukas @j23b0233
- Beck Cedric @j23b0826
- Brennförderer Timo @j23b0724
- Feyer Benjamin @j23f0712
- Fleischer Hanno @j23f0779
- Grigencha Daniel @j23g0274
- Koppe Felix @fkoppe

- Tutor: Krusch

## Was wurde besprochen:

- Soll der Spielstand speicherbar sein, sodass Spieler später weiterspielen können?

Antwort: Nein

</details>


# Implementierungsphase II

<details>
<summary markdown="span">19.11.2024</summary>

### Ort, Datum, Zeit

Gebäude 2 Haus 200 Raum Wohnebene, 19.11.2024, 22:30 - 22:50 Uhr

### Teilnehmer

- Bauer Lukas @j23b0233
- Beck Cedric @j23b0826
- Brennförderer Timo @j23b0724
- Feyer Benjamin @j23f0712
- Fleischer Hanno @j23f0779
- Grigencha Daniel @j23g0274
- Koppe Felix @fkoppe

- Tutor: Krusch

## Was wurde besprochen:

- Welche Fehler oder ungültigen Eingaben sollte das System erkennen und dem Spieler Rückmeldung geben?

Antwort: Nach eigenem Ermessen


</details>

<details>
<summary markdown="span">21.11.2024</summary>

### Ort, Datum, Zeit

Gebäude 2 Haus 200 Raum Wohnebene, 21.11.2024, 19:00 - 19:20 Uhr

### Teilnehmer

- Bauer Lukas @j23b0233
- Beck Cedric @j23b0826
- Brennförderer Timo @j23b0724
- Feyer Benjamin @j23f0712
- Fleischer Hanno @j23f0779
- Grigencha Daniel @j23g0274
- Koppe Felix @fkoppe

- Tutor: Krusch

## Was wurde besprochen:

- Sollen wir ein Logging-System einbauen, um den Verlauf des Spiels und potenzielle Fehler aufzuzeichnen?

Antwort: Nach eigenem Ermessen

</details>

<details>
<summary markdown="span">23.11.2024</summary>

### Ort, Datum, Zeit

Gebäude 2 Haus 200 Raum Wohnebene, 23.11.2024, 17:50 - 18:10 Uhr

### Teilnehmer

- Bauer Lukas @j23b0233
- Beck Cedric @j23b0826
- Brennförderer Timo @j23b0724
- Feyer Benjamin @j23f0712
- Fleischer Hanno @j23f0779
- Grigencha Daniel @j23g0274
- Koppe Felix @fkoppe

- Tutor: Krusch

## Was wurde besprochen:

Aktueller Stand des Projekts

</details>


# Implementierungsphase III