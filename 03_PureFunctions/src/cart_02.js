function addToCart(carItems, item) {
    return [...carItems, item];
}

const initalCart = ['Apple'];
const newCart = addToCart(initalCart, 'Banana');
console.log(newCart);