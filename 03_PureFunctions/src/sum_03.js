// 3.1
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
console.log("Sum:", sum_03(numList1)); // Gibt 21 aus
console.log("Mutated List:", numList1);         // Gibt [] aus (Array wurde geleert / mutiert!)

// 3.2
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

// 3.3
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
console.log("Sorted Strings:", sortStrings_03(myStrings));
console.log("Mutated Strings:", myStrings); // Gibt [] aus, da das Array mutiert wurde

// 3.4
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
console.log("Sorted Objects:", sortObjects_03(myObjects));
console.log("Verlauf:", comparisonLog);

// 3.5
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