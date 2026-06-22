# Aufgabe_03

### Was sind Pure Functions?
Eine pure function ist eine Funktion die bei gleichen Eingaben immer das gleiche Ergebnis liefert.
Ausserdem hat sie keine Nebenwirkungen, das heisst sie verändert nichts ausserhalb der Funktion.

### Aufgabe 1
| Aufgabe | Nur ein Rückgabewert | Resultat nur Abhängig von übergebenen Parametern | Verändert keine existierenden Werte | pure oder impure | Grund                                                                         |
|---------|----------------------|--------------------------------------------------|-------------------------------------|------------------|-------------------------------------------------------------------------------|
| 1.1     | Ja                   | Nein                                             | Nein                                | impure           | Verändert die externe Liste ```cartItems``` (Seiteneffekt).                   |
| 1.2     | Ja                   | Ja                                               | Ja                                  | pure             | Klassische mathematische Funktion.                                            |
| 1.3     | Ja                   | Ja                                               | Ja                                  | pure             | Nur Lesezugriff auf den String.                                               |
| 1.4     | Ja                   | Nein                                             | Ja                                  | impure           | Hängt vom Zufall ab (nicht deterministisch)                                   |
| 1.5     | Ja                   | Ja                                               | Ja                                  | pure             | Ergebnis ist immer gleich bei gleichen Inputs.                                |
| 1.6     | Ja                   | Ja                                               | Nein                                | impure           | ``println / console.log`` ist ein Seiteneffekt (Interkation mit der Konsole). |


### Aufgabe 2

#### 1.1
````js
function addToCart(carItems, item) {
    return [...carItems, item];
}

const initalCart = ['Apple'];
const newCart = addToCart(initalCart, 'Banana');
console.log(newCart);
````
Das Problem bei der impure function war, dass ``push`` das Original-Array ausserhalb der Funktion verändert hat.
Das konnte mit dem Spread-Operator gelöst werden. Dabei wird eine Kopie des alten Arrays erstellt und das Element anschliessend dieser Kopie hinzugefügt. Somit wird das Original nicht verändert.

#### 1.4
````js
// Methode, um eine Zahl mit einem zufälligen Wert zu multiplizieren
function multiply(number, factor) {
    return number * factor;
}

const randomValue = Math.random();
const result = multiply(10, randomValue);
console.log(result);
````
Das Problem bei der impure function war, dass der Rückgabewert auch von ``Math.random()`` abhängig war und nicht nur von ``number``.
Das konnte man lösen, indem man ``Math.random()`` aus der Funktion genommen hat. Somit ist diese nur noch von den Parametern abhängig.

#### 1.6
````js
// Methode zum Ausgeben und Rückgeben einer Zeichenkette
function returnStr(str) {
    return str;
}

const formatString = (str) => str;
console.log(returnStr(formatString("Hello")));
````
Das Problem bei der impure function war, dass innerhalb der Funktion auch etwas geprinted wurde und sie somit neben dem Rückgabewert noch einen Seiteneffekt auslöste.
Das konnte man lösen, indem man das ``println`` aus der Funktion entfernt und nur noch den String returned.

### Aufgabe 3 (Impure Functions)

Für diese Übungsaufgabe wurden die Funktionen so angepasst, dass sie **impure (unrein)** sind. Sie brechen die Regeln für pure functions (Seiteneffekte, Mutierung von Parametern, Abhängigkeit von externem Zustand).

#### 3.1
````js
let totalSum = 0; // Externer Zustand (Seiteneffekt)

function sum_03(numbers) {
    if (numbers.length === 0) return 0;
    
    // Seiteneffekt: shift() mutiert das übergebene Array direkt
    const head = numbers.shift(); 
    totalSum += head; // Seiteneffekt: Modifikation einer externen Variable
    
    return head + sum_03(numbers);
}

// Test
const numList1 = [1, 2, 3, 4, 5, 6];
console.log(sum_03(numList1)); // Gibt 21 aus
console.log(numList1);         // Gibt [] aus (Array wurde geleert / mutiert!)
````
**Warum diese Funktion impure ist:**
1. **Mutierung des Inputs:** Sie verwendet `shift()`, was das als Argument übergebene Array direkt verändert.
2. **Externer Zustand:** Sie modifiziert die globale Variable `totalSum`.

#### 3.2
````js
let averageCallCount = 0; // Externer Zustand

function average_03(numbers) {
    averageCallCount++; // Seiteneffekt: Modifikation einer externen Variable
    console.log(`Durchschnittsberechnung Aufruf #${averageCallCount}`); // Seiteneffekt: I/O (Konsolenausgabe)
    
    if (numbers.length === 0) return 0;
    
    // Da wir die impure sum_03 aufrufen, wird das numbers-Array hier mutiert!
    const total = sum_03(numbers);
    return total / (numbers.length + averageCallCount); 
}

// Test
const MyNumbers = [1, 2, 3, 4, 5];
console.log("Average:", average_03(MyNumbers));
````
**Warum diese Funktion impure ist:**
1. **Konsolenausgabe:** Sie nutzt `console.log`, was eine Interaktion mit der Aussenwelt (I/O) darstellt und somit ein Seiteneffekt ist.
2. **Abhängigkeit und Modifikation von externem Zustand:** Sie erhöht `averageCallCount` und nutzt diesen Wert in der Berechnung.
3. **Seiteneffekt-Vererbung:** Sie ruft die impure Funktion `sum_03` auf, welche das `MyNumbers`-Array komplett leert.

#### 3.3
````js
let sortCount = 0;

function sortStrings_03(list) {
    sortCount++; // Seiteneffekt: Globaler Zähler
    console.log(`Sortier-Durchlauf #${sortCount}`); // Seiteneffekt: I/O
    
    if (list.length <= 1) return list;
    
    // Seiteneffekt: pop() mutiert das Original-Array direkt
    const pivot = list.pop(); 
    
    const small = [];
    const big = [];
    
    // Seiteneffekt: shift() leert das Original-Array
    while (list.length > 0) {
        const s = list.shift();
        if (s.toLowerCase() < pivot.toLowerCase()) {
            small.push(s);
        } else {
            big.push(s);
        }
    }
    
    return [...sortStrings_03(small), pivot, ...sortStrings_03(big)];
}

// Test
const myStrings = ["Hello", "Baum", "apprentice"];
console.log(sortStrings_03(myStrings));
console.log(myStrings); // Gibt [] aus, da das Array mutiert wurde
````
**Warum diese Funktion impure ist:**
1. **Veränderung der Eingabewerte:** Die Verwendung von `pop()` und `shift()` leert das ursprüngliche Array komplett.
2. **Nebeneffekte durch Protokollierung und Zähler:** Sie verändert den globalen Zähler `sortCount` und schreibt in die Konsole.

#### 3.4
````js
let comparisonLog = []; // Externer Zustand

const sortObjects_03 = (list) => {
    if (list.length <= 1) return list;
    
    // Seiteneffekt: shift() mutiert das Original-Array
    const pivot = list.shift(); 
    
    const isSmaller = (a, b) => {
        // Seiteneffekt: Schreiben in ein externes Array
        comparisonLog.push(`Vergleiche: ${a.titel} mit ${b.titel}`); 
        
        if (a.date !== b.date) return a.date < b.date;
        if (a.prio !== b.prio) return a.prio < b.prio;
        return a.titel < b.titel;
    }
    
    const small = [];
    const big = [];
    
    while(list.length > 0) {
        const item = list.shift(); // Seiteneffekt
        if (isSmaller(item, pivot)) {
            small.push(item);
        } else {
            big.push(item);
        }
    }
    
    return [...sortObjects_03(small), pivot, ...sortObjects_03(big)];
}

// Test
const myObjects = [
    {date: "2026-01-01", prio: 2, titel: "B-Aufgabe"},
    {date: "2026-01-01", prio: 1, titel: "A-Aufgabe"},
    {date: "2025-02-01", prio: 5, titel: "Alt-Aufgabe"},
];
console.log(sortObjects_03(myObjects));
console.log("Verlauf:", comparisonLog);
````
**Warum diese Funktion impure ist:**
1. **Mutierung des Eingabeparameters:** Sie mutiert das Array direkt via `shift()`.
2. **Schreiben in externen Speicher:** Sie befüllt das globale `comparisonLog`-Array bei jedem Vergleich.

#### 3.5
````js
let totalLeavesFound = 0; // Externer Zustand

function getLeaves_03(node) {
    // Seiteneffekt: Wir fügen dem Knoten direkt eine Eigenschaft hinzu (Mutierung des Inputs)
    node.visited = true; 
    
    if (!node.children || node.children.length === 0) {
        totalLeavesFound++; // Seiteneffekt: Modifikation einer externen Variable
        return [node];
    }
    
    // Seiteneffekt: Wir mutieren das children-Array des Knotens durch pop()
    const child = node.children.pop(); 
    const leaves = getLeaves_03(child);
    
    // Rekursion über die verbleibenden Kinder
    return [...leaves, ...getLeaves_03(node)];
}

// Test
const myTree = {
    name: "Stamm",
    children: [
        {
            name: "Ast A",
            children: [
                { name: "Blatt A1" },
                { name: "Blatt A2" }
            ]
        },
        {
            name: "Ast B",
            children: []
        }
    ]
};

const leaves = getLeaves_03(myTree);
console.log("Gefundene Blätter:", leaves);
console.log("Anzahl Blätter (Globaler Zähler):", totalLeavesFound);
console.log("Modifizierter Baum (Seiteneffekt):", myTree); // Besitzt nun .visited-Properties und geleerte Kinder
````
**Warum diese Funktion impure ist:**
1. **Mutierung von Objekten:** Sie fügt dem `node` direkt eine neue Property `visited` hinzu.
2. **Mutierung der Objekt-Struktur:** Sie leert das `children`-Array der Knoten durch `pop()`.
3. **Modifikation von globalem Zustand:** Sie inkrementiert die externe Variable `totalLeavesFound`.