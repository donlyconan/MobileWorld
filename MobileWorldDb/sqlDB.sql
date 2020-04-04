create database MobileWorld
create table category
(
	categoryid int primary key auto_increment,
    categoryname nvarchar(255) not null,
    categoryimage varchar(255) not null
)

create table products 
(
	id int primary key auto_increment,
    name nvarchar(255) not null,
    price bigint not null,
    image varchar(255) not null,
    description text not null,
    categoryid int not null,
    CONSTRAINT FK_ProductsCategory FOREIGN KEY (categoryid)
    REFERENCES category(categoryid)
)

create table user 
(
	userid int primary key auto_increment,
	phonename varchar(15) not null,
    password nvarchar(50) not null,
    username nvarchar(50) not null
)

create table manageuser
(
	id int primary key auto_increment,
    userid int not null,
    summoney bigint not null,
    orderdate timestamp,
    CONSTRAINT FK_ManageuserUser FOREIGN KEY (userid)
    REFERENCES user(userid)
)

insert into user(phonename,password,username) values ("0969498528", "tuan123", "le huy tuan")

insert into category(categoryname,categoryimage) values ("Điện thoại", "https://www.freeiconspng.com/uploads/cell-phone-icon-png--clipart-best-19.png")
insert into category(categoryname,categoryimage) values ("Laptop", "https://www.freeiconspng.com/uploads/laptop-png-23.png")

--insert từng dòng một, ko biết mysql có thể inesrt đc nhiều dòng 1 lần ko?
insert into products(name, price, image, description, categoryid) values ("Laptop Acer Swift 3 SF315 52 38YQ i3", 11490000, "https://cdn.tgdd.vn/Products/Images/44/208863/acer-swift-sf315-52-38yq-i3-8130u-4gb-1tb-156f-win-600x600.jpg", "Acer Swift 3 SF315 là mẫu laptop văn phòng được tích hợp sẵn Windown 10 với cấu hình khá, pin dùng cực lâu đến 11 tiếng. Thiết kế của máy sang trọng, lại còn gọn nhẹ, dễ dàng di chuyển, phù hợp với giới sinh viên, nhân viên văn phòng phải mang theo laptop hàng ngày.", 2)
insert into products(name, price, image, description, categoryid) values ("Laptop Acer Swift 3 SF315 52 38YQ i3", 11490000, "https://cdn.tgdd.vn/Products/Images/44/208863/acer-swift-sf315-52-38yq-i3-8130u-4gb-1tb-156f-win-600x600.jpg", "Acer Swift 3 SF315 là mẫu laptop văn phòng được tích hợp sẵn Windown 10 với cấu hình khá, pin dùng cực lâu đến 11 tiếng. Thiết kế của máy sang trọng, lại còn gọn nhẹ, dễ dàng di chuyển, phù hợp với giới sinh viên, nhân viên văn phòng phải mang theo laptop hàng ngày.", 2)
insert into products(name, price, image, description, categoryid) values ("Laptop Acer Swift 3 SF315 52 38YQ i3", 11490000, "https://cdn.tgdd.vn/Products/Images/44/208863/acer-swift-sf315-52-38yq-i3-8130u-4gb-1tb-156f-win-600x600.jpg", "Acer Swift 3 SF315 là mẫu laptop văn phòng được tích hợp sẵn Windown 10 với cấu hình khá, pin dùng cực lâu đến 11 tiếng. Thiết kế của máy sang trọng, lại còn gọn nhẹ, dễ dàng di chuyển, phù hợp với giới sinh viên, nhân viên văn phòng phải mang theo laptop hàng ngày.", 2)
insert into products(name, price, image, description, categoryid) values ("Laptop Acer Swift 3 SF315 52 38YQ i3", 11490000, "https://cdn.tgdd.vn/Products/Images/44/208863/acer-swift-sf315-52-38yq-i3-8130u-4gb-1tb-156f-win-600x600.jpg", "Acer Swift 3 SF315 là mẫu laptop văn phòng được tích hợp sẵn Windown 10 với cấu hình khá, pin dùng cực lâu đến 11 tiếng. Thiết kế của máy sang trọng, lại còn gọn nhẹ, dễ dàng di chuyển, phù hợp với giới sinh viên, nhân viên văn phòng phải mang theo laptop hàng ngày.", 2)
insert into products(name, price, image, description, categoryid) values ("Laptop Acer Swift 3 SF315 52 38YQ i3", 11490000, "https://cdn.tgdd.vn/Products/Images/44/208863/acer-swift-sf315-52-38yq-i3-8130u-4gb-1tb-156f-win-600x600.jpg", "Acer Swift 3 SF315 là mẫu laptop văn phòng được tích hợp sẵn Windown 10 với cấu hình khá, pin dùng cực lâu đến 11 tiếng. Thiết kế của máy sang trọng, lại còn gọn nhẹ, dễ dàng di chuyển, phù hợp với giới sinh viên, nhân viên văn phòng phải mang theo laptop hàng ngày.", 2)
insert into products(name, price, image, description, categoryid) values ("Laptop Acer Swift 3 SF315 52 38YQ i3", 11490000, "https://cdn.tgdd.vn/Products/Images/44/208863/acer-swift-sf315-52-38yq-i3-8130u-4gb-1tb-156f-win-600x600.jpg", "Acer Swift 3 SF315 là mẫu laptop văn phòng được tích hợp sẵn Windown 10 với cấu hình khá, pin dùng cực lâu đến 11 tiếng. Thiết kế của máy sang trọng, lại còn gọn nhẹ, dễ dàng di chuyển, phù hợp với giới sinh viên, nhân viên văn phòng phải mang theo laptop hàng ngày.", 2)
insert into products(name, price, image, description, categoryid) values ("Laptop Acer Swift 3 SF315 52 38YQ i3", 11490000, "https://cdn.tgdd.vn/Products/Images/44/208863/acer-swift-sf315-52-38yq-i3-8130u-4gb-1tb-156f-win-600x600.jpg", "Acer Swift 3 SF315 là mẫu laptop văn phòng được tích hợp sẵn Windown 10 với cấu hình khá, pin dùng cực lâu đến 11 tiếng. Thiết kế của máy sang trọng, lại còn gọn nhẹ, dễ dàng di chuyển, phù hợp với giới sinh viên, nhân viên văn phòng phải mang theo laptop hàng ngày.", 2)
insert into products(name, price, image, description, categoryid) values ("Laptop Acer Swift 3 SF315 52 38YQ i3", 11490000, "https://cdn.tgdd.vn/Products/Images/44/208863/acer-swift-sf315-52-38yq-i3-8130u-4gb-1tb-156f-win-600x600.jpg", "Acer Swift 3 SF315 là mẫu laptop văn phòng được tích hợp sẵn Windown 10 với cấu hình khá, pin dùng cực lâu đến 11 tiếng. Thiết kế của máy sang trọng, lại còn gọn nhẹ, dễ dàng di chuyển, phù hợp với giới sinh viên, nhân viên văn phòng phải mang theo laptop hàng ngày.", 2)
insert into products(name, price, image, description, categoryid) values ("Laptop Acer Swift 3 SF315 52 38YQ i3", 11490000, "https://cdn.tgdd.vn/Products/Images/44/208863/acer-swift-sf315-52-38yq-i3-8130u-4gb-1tb-156f-win-600x600.jpg", "Acer Swift 3 SF315 là mẫu laptop văn phòng được tích hợp sẵn Windown 10 với cấu hình khá, pin dùng cực lâu đến 11 tiếng. Thiết kế của máy sang trọng, lại còn gọn nhẹ, dễ dàng di chuyển, phù hợp với giới sinh viên, nhân viên văn phòng phải mang theo laptop hàng ngày.", 2)