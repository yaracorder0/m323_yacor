# Aufgabe_02

### Anforderungen als Funktionen erkennen

#### Reise Planen

````java
addDestination(List<String> route, String destination){}
changeDestination(List<String> route, int index, String newDestination){}
removeDestination(List<String> route, int index) {}
getRoute(List<String> route){}
````
``addDestination()`` fügt ein neues Reieziel hinzu.
<br>
``changeDestination()`` ändert das bisherige Reiseziel zu einem neuen.
<br>
``removeDestination()`` entfernt ein bereits eingegebenes Reiseziel.
<br>
``getRoute()`` gibt die aktuelle Route zurück.

#### Wörter mit Punkten bewerten

````java
enterWord(String word) {}
calculateScore(String word) {}
sortWords(List<String> words) {}
getWords(List<String> words) {}
````
``enterWord()`` nimmt die Eingabe des Benutzers entgegen.
<br>
``calculateScore()`` berechnet die Punktzahl eines Wortes, wobei der Buchstabe ``a`` nicht gezählt wird.
<br>
``sortWords()`` sortiert die Wörter nach der höchsten Punktzahl.
<br>
``getWords()`` gibt alle gespeicherten Wörter zurück.

#### Autorennen

````java
calculateTime(List<Double> lapTimes) {}
calculateAverageTime(List<Double> lapTimes) {}
addLapTime(List<Double> lapTimes, double time) {}
getLapTimes(List<Double> lapTimes) {}
````
``calculateTime()`` berechnet die gesamte Rennzeit aller Runden, wobei die erste Runde nicht mitgezählt wird
<br>
``calculateAverageTime()`` berechnet die Durchschnittszeit aller gewerteten Runden ohne die erste Runde.
<br>
``addLapTime()`` fügt eine neue Rundezeit hinzu
<br>
``getLapTimes()`` gibt alle gespeicherten Rundenzeiten zurück