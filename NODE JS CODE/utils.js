const fs = require("fs");

/**
 * Function to read and parse JSON data from a file.
 * @param {string} filename - JSON file name.
 * @returns {Object} - Parsed JSON data.
 */
function readJSON(filename) {
    return JSON.parse(fs.readFileSync(filename, "utf8"));
}

/**
 * Function to decode values from different bases.
 * @param {string} value - Encoded string.
 * @param {number} base - Base to decode from.
 * @returns {number} - Decoded integer value.
 */
function decodeValue(value, base) {
    return parseInt(value, base);
}

/**
 * Lagrange Interpolation to find the constant term (c).
 * @param {Array} points - Array of (x, y) pairs.
 * @returns {number} - The constant term c.
 */
function lagrangeInterpolation(points) {
    let secret = 0;

    for (let i = 0; i < points.length; i++) {
        let xi = points[i][0];
        let yi = points[i][1];

        let li = 1;
        for (let j = 0; j < points.length; j++) {
            if (i !== j) {
                let xj = points[j][0];
                li *= xj / (xj - xi);
            }
        }

        secret += yi * li;
    }

    return Math.round(secret);
}

/**
 * Function to process the JSON test case.
 * @param {string} filename - JSON file name.
 * @returns {number} - The constant term c.
 */
function processTestCase(filename) {
    const data = readJSON(filename);
    const { n, k } = data.keys;

    // Collect k roots (minimum required for solving the polynomial)
    let points = [];
    let count = 0;
    for (let key in data) {
        if (key !== "keys" && count < k) {
            let x = parseInt(key);
            let base = parseInt(data[key]["base"]);
            let y = decodeValue(data[key]["value"], base);
            points.push([x, y]);
            count++;
        }
    }

    return lagrangeInterpolation(points);
}

module.exports = { processTestCase };
