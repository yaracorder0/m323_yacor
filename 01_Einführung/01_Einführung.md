# Aufgabe_01

### Bei welcher Funktion haben wir keine Garantie, dass wir einen Wert zurückerhalten?

````java
public static int add(int a, int b) {
	return a + b;
}
public static char getFirstCharacter(String s) {
	return s.charAt(0);
}
````
Bei der Funktion ``getFirstCharacter(String s)`` hat man keine Garantie, dass man immer einen Wert zurückerhält.
Wenn der String leer ist ```("")``` oder ``null`` übergeben wird, entsteht ein Fehler (Exception). In diesem Fall wird kein Wert zurückgegeben.

Dieses Problem könnte man beispielsweise mit ``Optional.ofNullable()`` lösen. Dabei wird nur dann ein Wert zurückgegeben, wenn einer vorhanden ist.

Bei der Methode ``add(int a, int b)`` hingegen wird immer ein Wert zurückgegeben, solange gültige int-Werte übergeben werden.