const fs = require("fs");
function readJSON(filename) {
    return JSON.parse(fs.readFileSync(filename, "utf8"));
}
function decodeValue(value, base) {
    return parseInt(value, base);
}
function lagrangeInterpolation(points) {
    let secret = 0;

    for (let i = 0; i < points.length; i++) {
        let li = 1;
        for (let j = 0; j < points.length; j++) {
            if (i !== j) {
                li *= (0 - points[j].x) / (points[i].x - points[j].x);
            }
        }
        secret += points[i].y * li;
    }

    return Math.round(secret); 
}
function processTestCase(filename) {
    const data = readJSON(filename);
    const k = data.keys.k;

    let points = [];
    let count = 0;

    for (let key in data) {
        if (key !== "keys" && count < k) {
            let x = parseInt(key);
            let base = parseInt(data[key].base);
            let y = decodeValue(data[key].value, base);
            points.push({ x, y });
            count++;
        }
    }

    return lagrangeInterpolation(points);
}
module.exports = { processTestCase };
