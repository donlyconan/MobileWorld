const express = require("express");
const db = require("./connectDb");
const bodyParser = require("body-parser");

const app = express();
const port = 8081;

app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());

//lay tat ca san pham
app.get("/getAllProduct", (req, res) => {
  db.query("SELECT * FROM products", (err, values) => {
    if (err) throw err;
    res.json(values);
  });
});

//lay san pham moi nhat (by category & page number)
app.get("/getProductNewest/:categoryid?", (req, res) => {
  let space = 6; // gioi han phan tu tren 1 trang
  let page = req.query.page || 1;
  let limit = 0; //phan tu bat dau
  let categoryid = req.params.categoryid;
  let sql = `SELECT * FROM products`;
  if (categoryid != null) {
    limit = (page - 1) * --space;
    sql += ` WHERE categoryid = ${categoryid}`;
  }
  sql += ` ORDER BY id DESC LIMIT ${limit},${space}`;
  db.query(sql, (err, values) => {
    if (err) throw err;
    res.json(values);
  });
});

//lay san pham theo id
app.get("/getProduct/:id", (req, res) => {
  db.query(
    `SELECT * FROM products WHERE id = '${req.params.id}'`,
    (err, values) => {
      if (err) throw err;
      res.json(values);
    }
  );
});

//lay tat ca the loai
app.get("/getAllCategory", (req, res) => {
  db.query("SELECT * FROM category", (err, values) => {
    if (err) throw err;
    res.json(values);
  });
});

//lay the loai theo id
app.get("/getCategory/:id", (req, res) => {
  db.query(
    `SELECT * FROM category WHERE categoryid = '${req.params.id}'`,
    (err, values) => {
      if (err) throw err;
      res.json(values);
    }
  );
});

//add new product
app.post("/addProduct", (req, res) => {
  let newProduct = req.body;
  let sql = `INSERT INTO products SET ?`;
  db.query(sql, newProduct, err => {
    if (err) throw err;
  });
  res.sendStatus(200);
});

//add new user
app.post("/addUser", (req, res) => {
  let newUser = req.body;
  let sql = "SELECT COUNT(*) FROM user WHERE phonename = ?";
  let count = 0;

  db.query(sql, newUser.phonename, (err, values) => {
    if (err) throw err;
    count = values[0]["COUNT(*)"];
    if (count !== 0) {
      res.json({
        phonename: newUser.phonename
      });
    } else {
      let sql = `INSERT INTO user SET ?`;
      db.query(sql, newUser, err => {
        if (err) throw err;
        res.sendStatus(200);
      });
    }
  });
});

//login
app.post("/login", (req, res) => {
  let acc = req.body;
  let sql = "SELECT * FROM user WHERE phonename = ? LIMIT 1";

  db.query(sql, acc.phonename, (err, values) => {
    if (err) throw err;
    if (values.length > 0) {
      if (values[0].password === acc.password) {
        let summoney = parseInt(acc.summoney);
        let sql = `INSERT INTO manageuser(userid,summoney,orderdate) VALUES (${values[0].userid}, ${summoney}, now())`;
        db.query(sql, (err, values) => {
          if (err) throw err;
          res.sendStatus(200);
        });
      } else {
        res.send("Mật khẩu sai");
      }
    } else {
      res.send("Tài khoản không tồn tại");
    }
  });
});

//sum monney
app.post("/summoney", (req, res) => {
  let sql =
    "INSERT INTO `managemoney`(summoney) VALUES (SELECT SUM(summoney) FROM `manageuser` WHERE CURRENT_DATE = DATE(orderdate))";
  db.query(sql, (err, values) => {
    if (err) throw err;
    res.sendStatus(200);
  });
});

app.listen(port, () => console.log(`Server started on port ${port}`));
