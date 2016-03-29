create database whitebox_db;
use  whitebox_db;

--------------------------------------------------------------------
-- Tables
--------------------------------------------------------------------
CREATE TABLE `customer_info` (

  `Build_ID` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(128) NOT NULL,
  `Email` varchar(128) DEFAULT NULL,
  `Phone` varchar(45) DEFAULT NULL,
  `Address` varchar(128) DEFAULT NULL,
  `Delivery_Date` date NOT NULL,	
  
  PRIMARY KEY (`Build_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



CREATE TABLE `payment_info` (

  `Build_ID` int(11) NOT NULL,
  `Payment_method_use` varchar(45) NOT NULL,
  `Total_value` int(11) NOT NULL,
  `Delivery_confirmation_link` varchar(45) NOT NULL,
  
  KEY `FK_Payment_info_idx` (`Build_ID`),
  CONSTRAINT `FK_Payment_info` FOREIGN KEY (`Build_ID`) REFERENCES `customer_info` (`Build_ID`) 
  ON DELETE CASCADE 
  ON UPDATE NO ACTION
  
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



CREATE TABLE `product_info` (
  `Build_ID` int(11) NOT NULL,
  `Component_type` int(11) NOT NULL,
  `Manufacturer` int(11) NOT NULL,
  `Product_description` varchar(45) NOT NULL,
  `model_number` int(11)  NOT NULL,
  `Serial_number` int(11)  NOT NULL,
  `Rebate_value` int(11) DEFAULT NULL,
  `Price` int(11) NOT NULL,
  `Warranty_period` int(11) DEFAULT NULL,
  `Warranty_expire` date DEFAULT NULL,
  `Invoice_date` date NOT NULL,
  `Invoice_number` int(11) NOT NULL,
  `Sale_order_number` int(11) NOT NULL,
  `Item_SKU` int(11) NOT NULL,
  
  KEY `FK_Product_info_idx` (`Build_ID`),
  CONSTRAINT `FK_Product_info` FOREIGN KEY (`Build_ID`) REFERENCES `customer_info` (`Build_ID`) 
  ON DELETE CASCADE 
  ON UPDATE NO ACTION
  
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



CREATE TABLE `component_type_table` (
  `Component_type` int(11) NOT NULL,
  `Component_type_description` varchar(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `manufacturer_table` (
  `Manufacturer` int(11) NOT NULL,
  `Manufacturer_name` varchar(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;




-- Stored Procedure which should be used as interface to the database.

-- --------------------------------------------------------------------
-- get_customer_info_by_name()
-- IN:	Customer Name. Wildcard is allowed such as 'Smit%'
-- Return: Customer Name, Address, Email, Phone and Delivery_Date
-- --------------------------------------------------------------------
DELIMITER $$
CREATE PROCEDURE `get_customer_by_name`(IN customer_name varchar(128))
BEGIN
	SELECT CINF.Name, CINF.Address, CINF.Email, CINF.Phone, CINF.Delivery_Date 
    FROM whitebox_db.customer_info CINF
    WHERE Name LIKE customer_name
	ORDER BY CINF.Name;
END$$
DELIMITER ;


-- --------------------------------------------------------------------
-- get_customer_payment_info_by_name()
-- IN:	Customer Name. Wildcard is allowed such as 'Smit%'
-- Return: Customer Name, Payment_method_use, Total_value and Delivery_confirmation_link
-- --------------------------------------------------------------------
DELIMITER $$
CREATE PROCEDURE `get_customer_payment_by_name`(IN customer_name varchar(128))
BEGIN
	SELECT CINF.Name, PYINF.Payment_method_use, PYINF.Total_value, PYINF.Delivery_confirmation_link 
    FROM whitebox_db.customer_info CINF
    JOIN whitebox_db.payment_info PYINF on CINF.Build_ID = PYINF.Build_ID
    WHERE Name LIKE customer_name
	ORDER BY CINF.Name;
END$$
DELIMITER ;


-- --------------------------------------------------------------------
-- get_customer_payment_info_by_name()
-- IN:	Customer Name. Wildcard is allowed such as 'Smit%'
-- Return: 	Customer Name,  		Component_type_description, 	Manufacturer_name, 
-- 			Product_description, 	PRINF.model_number, 			Serial_number,					
-- 			Rebate_value, 			Price, 							Warranty_period,	
-- 			Warranty_period, 		Invoice_date, 					Invoice_number,			
-- 			Sale_order_number, 		Item_SKU
-- --------------------------------------------------------------------
DELIMITER $$
CREATE PROCEDURE `get_customer_product_by_name`(IN customer_name varchar(128))
BEGIN
	SELECT 
		CINF.Name, 					COMTBL.Component_type_description, 		MANTBL.Manufacturer_name,
		PRINF.Product_description, 	PRINF.model_number, 					PRINF.Serial_number,
        PRINF.Rebate_value, 		PRINF.Price, 							PRINF.Warranty_period,
        PRINF.Warranty_period, 		PRINF.Invoice_date, 					PRINF.Invoice_number,
        PRINF.Sale_order_number, 	PRINF.Item_SKU
    FROM whitebox_db.customer_info CINF
    JOIN whitebox_db.product_info PRINF on CINF.Build_ID = PRINF.Build_ID
    JOIN whitebox_db.manufacturer_table MANTBL ON PRINF.Manufacturer = MANTBL.Manufacturer
	JOIN whitebox_db.component_type_table COMTBL ON PRINF.Component_type = COMTBL.Component_type
    WHERE Name LIKE customer_name 
    ORDER BY CINF.Name;
END$$
DELIMITER ;


-- --------------------------------------------------------------------
-- delete_customer_info_by_name()
-- It deletes the customer entry, and also delete the payment info and product info of this customer.
-- IN:	Customer Name. Wildcard is allowed such as 'Smit%'
-- Return: none
-- --------------------------------------------------------------------
DELIMITER $$
CREATE PROCEDURE `delete_customer_by_name`(IN customer_name varchar(128))
BEGIN
	DELETE from whitebox_db.customer_info WHERE Name LIKE customer_name;
END$$
DELIMITER ;



DELIMITER $$
CREATE PROCEDURE `create_customer_by_name`(
	IN customer_name varchar(128),
    IN email varchar(128),
    IN phone varchar(45),
    IN address varchar(128),
    IN Delivery_Date date
)
BEGIN
	insert into whitebox_db.customer_info values (
	default, customer_name, email, phone, address, Delivery_Date);
END$$
DELIMITER ;


-- Never declare any Variable within START TRANSACTION .. COMMIIT Block.
-- Exit Handler Declaration should be your last Declaration just ahead of START TRANSACTION .. COMMIIT Block.
DELIMITER $$
CREATE PROCEDURE `create_customer_by_name_with_payment_info`(
	IN customer_name varchar(128),
    IN email varchar(128),
    IN phone varchar(45),
    IN address varchar(128),
    IN Delivery_Date date,
    
    IN	Payment_method_use varchar(45),
	IN  Total_value int(11),
	IN	Delivery_confirmation_link varchar(45)
)
BEGIN
	DECLARE id INT;
    
    DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
	DECLARE EXIT HANDLER FOR SQLWARNING ROLLBACK;

	START TRANSACTION;
		insert into whitebox_db.customer_info values (default, customer_name, email, phone, address, Delivery_Date);
		SELECT Build_ID INTO id from whitebox_db.customer_info where whitebox_db.customer_info.Name = customer_name;
		SELECT id;	-- It returns id value. Actually, this is not needed. Just good for debug
		INSERT into whitebox_db.payment_info values (id, Payment_method_use, Total_value, Delivery_confirmation_link);
	COMMIT;
END$$
DELIMITER ;
