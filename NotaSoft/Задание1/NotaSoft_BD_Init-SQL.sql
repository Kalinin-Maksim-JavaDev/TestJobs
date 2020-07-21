DROP TABLE "notaSoft"."OrderItem";
DROP TABLE "notaSoft"."Order";
DROP TABLE "notaSoft"."GoodsCategories";
DROP TABLE "notaSoft"."Сategories";
DROP TABLE "notaSoft"."Goods";
DROP TABLE "notaSoft"."Customer";

CREATE TABLE "notaSoft"."Customer"
(
 "CustomerId"   integer NOT NULL ,
 "CustomerName" varchar(50) NOT NULL,
 CONSTRAINT "PK_Customer" PRIMARY KEY ( "CustomerId" ),
 CONSTRAINT "AK1_Customer_CustomerName" UNIQUE ( "CustomerName" )
);
CREATE TABLE "notaSoft"."Goods"
(
 "GoodId"      integer NOT NULL,
 "Description" varchar(50) NOT NULL,
 CONSTRAINT "PK_Goods" PRIMARY KEY ( "GoodId" )
);
CREATE TABLE "notaSoft"."Сategories"
(
 "CategoryId"  integer NOT NULL,
 "Description" varchar(50) NOT NULL,
 "Parentid"    integer NOT NULL,
 CONSTRAINT "PK_Сategories" PRIMARY KEY ( "CategoryId" ),
 CONSTRAINT "FK_132" FOREIGN KEY ( "Parentid" ) REFERENCES "notaSoft"."Сategories" ( "CategoryId" )
);
CREATE TABLE "notaSoft"."GoodsCategories"
(
 "CategoryId" integer NOT NULL,
 "GoodId"     integer NOT NULL,
 CONSTRAINT "FK_204" FOREIGN KEY ( "CategoryId" ) REFERENCES "notaSoft"."Сategories" ( "CategoryId" ),
 CONSTRAINT "FK_207" FOREIGN KEY ( "GoodId" ) REFERENCES "notaSoft"."Goods" ( "GoodId" )
);
CREATE TABLE "notaSoft"."Order"
(
 "OrderId"     integer NOT NULL,
 "OrderNumber" varchar(10) NULL,
 "CustomerId"  integer NOT NULL,
 "OrderDate"   timestamp NOT NULL,
 "TotalAmount" decimal(12,2) NOT NULL,
 CONSTRAINT "PK_Order" PRIMARY KEY ( "OrderId" ),
 CONSTRAINT "AK1_Order_OrderNumber" UNIQUE ( "OrderNumber" ),
 CONSTRAINT "FK_Order_CustomerId_Customer" FOREIGN KEY ( "CustomerId" ) REFERENCES "notaSoft"."Customer" ( "CustomerId" )
);
CREATE TABLE "notaSoft"."OrderItem"
(
 "OrderId"   integer NOT NULL,
 "UnitPrice" decimal(12,2) NOT NULL,
 "Quantity"  integer NOT NULL,
 "GoodId"    integer NOT NULL,
 CONSTRAINT "PK_OrderItem" PRIMARY KEY ( "OrderId" ),
 CONSTRAINT "FK_OrderItem_OrderId_Order" FOREIGN KEY ( "OrderId" ) REFERENCES "notaSoft"."Order" ( "OrderId" ),
 CONSTRAINT "FK_193" FOREIGN KEY ( "GoodId" ) REFERENCES "notaSoft"."Goods" ( "GoodId" )
);