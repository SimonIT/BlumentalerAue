# BlumentalerAue

## Bewertung

a) Erstellen Sie eine Dokumentation zum Entwurf der Applikation, die objektorientiert und modularisiert
implementiert werden soll! Diese sind schriftlich abzugeben und beinhalten ein Klassendiagramm.(15 Punkte)
b) Implementieren Sie ein Programmfenster mit einer Menüleiste, in der das Programm beendet
werden kann und Informationen über den Verfasser beinhaltet! (10 Punkte)

c) Erstellen Sie Controls, die es dem Anwender ermöglichen, SQL-basierte Datenbankabfragen
zu steuern, ohne dass der Anwender SQL-Anweisungen eingeben muss! Die Verbindung zur Datenbank wird über eine Menüeintrag hergestellt.Eine erfolgreiche Verbindung wir in der Statuszeile ausgewiesen. (20 Punkte)

d) Implementieren Sie ein geeignetes Control und die entsprechende Programmlogik in der Anwendung,
um ein Pflanzenschutzmittel (z.B.Altrazin oder Simazin) für die Filterung bei der Datenbankabfrage
auszuwählen! (15 Punkte)

e) Erstellen Sie ein Control „Daten lesen“, welches dem Anwender ermöglicht, die Messwerte aus
der Datenbank auszulesen. Die Messwerte werden nach der Datenbankspalte „Index“ aufsteigend
sortiert gelesen und werden daraufhin in geeigneten Controls als Zahlenwerte und in einem
Diagramm angezeigt. Die x-Achse des Diagramms ist der Index und die y-Achse die Konzentration
des Pflanzenschutzmittels (Messwert). (25 Punkte)

f) Die Applikation enthält die Möglichkeit den Pfad zu Datenbank auszuwählen. (5 Punkte)

g) Beim Beenden der Applikation  werden einige Einstellungen in einer Datei gespeichert (ini, Property, json ...). Sollte es diese Datei nicht geben so startet ihr Programm mit Standarwerten. Beim Beendigen des Programms wird die Einstellungsdatei erstellt.Diese Einstellungen werden beim nächsten Start berücksichtigt.(Pfad zur DB, Fenstergröße und Position) (10 Punkte)

## Anleitung

Zum Starten einfach eine der beiden Run Configurations in IntelliJ starten oder `.\gradlew.bat run` bzw `./gradlew run` ausführen.
