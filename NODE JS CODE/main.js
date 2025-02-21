const { processTestCase } = require("./utils");

// Process both test cases
const secret1 = processTestCase("testcase1.json");
const secret2 = processTestCase("testcase2.json");

// Output results
console.log(`Secret for Test Case 1: ${secret1}`);
console.log(`Secret for Test Case 2: ${secret2}`);
