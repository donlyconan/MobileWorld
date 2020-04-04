const mysql = require("mysql");

const db = mysql.createConnection({
  host: "localhost",
  user: "root",
  password: "tuan123",
  database: "mobileworld"
});

db.connect(err => {
  if (err) throw err;
  console.log("MySql connected...");
});

module.exports = db;
