// 3.1
function sum_03(numbers) {
    if (numbers.length === 0) return 0;
    const [head, ...tail] = numbers;
    return head + sum_03(tail);
}
// Test
console.log(sum_03([]));
console.log(sum_03([1,2,3,4,5,6]));
console.log(sum_03([10, -5, 2]));
console.log(sum_03([0,0,0]));

// 3.2
function average_03(numbers, length = numbers.length) {
    if (numbers.length === 0) return 0;
    return sum_03(numbers) / length;
}
// Test
const MyNumbers = [1, 2, 3, 4, 5];
console.log("Average:", average_03(MyNumbers));

// 3.3
function sortStrings_03(list) {
    if (list.length <= 1) return list;
    const [pivot, ...rest] = list;

    const small = rest.filter(s => s.toLowerCase() < pivot.toLowerCase());
    const big = rest.filter(s => s.toLowerCase() >= pivot.toLowerCase());

    return [...sortStrings_03(small), pivot, ...sortStrings_03(big)]
}
// Test
const myStrings = ["Hello", "Baum", "apprentice"];
console.log(sortStrings_03(myStrings))

// 3.4
const sortObjects_03 = (list) => {
    if (list.length <= 1) return list;
    const [pivot, ...rest] = list;

    const isSmaller = (a, b) => {
        if (a.date !== b.date) return a.date < b.date;
        if (a.prio !== b.prio) return a.prio < b.prio;
        return a.titel < b.titel;
    }

    const small = rest.filter(item => isSmaller(item, pivot));
    const big = rest.filter(item => !isSmaller(item, pivot));

    return [...sortObjects_03(small), pivot, ...sortObjects_03(big)];
}
// Test
const myObjects = [
    {date: "2026-01-01", prio: 2, titel: "B-Aufgabe"},
    {date: "2026-01-01", prio: 1, titel: "A-Aufgabe"},
    {date: "2025-02-01", prio: 5, titel: "Alt-Aufgabe"},
];
console.log(sortObjects_03(myObjects));

// 3.5
function getLeaves_03(node) {
    if (!node.children || node.children.length === 0) {
        return [node];
    }

    return node.children.flatMap(child => getLeaves_03(child));
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
        },
        {
            name: "Ast C",
            children: [
                {
                    name: "Zweig C1",
                    children: [{ name: "Blatt C1-1" }]
                }
            ]
        }
    ]
};

const leaves = getLeaves_03(myTree);
console.log("Anzahl der gefundenen Blätter:", leaves.length);
console.log("Namen der Blätter:", leaves.map(b => b.name));