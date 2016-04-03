use Whitebox_db;

-- drop database whitebox_db;

-- drop table whitebox_db.customer_info;
-- drop table whitebox_db.payment_info;
-- drop table whitebox_db.product_info;

-- drop table whitebox_db.manufacturer_name;
-- drop table whitebox_db.component_type_description;

-- delete from  whitebox_db.product_info where build_id =2;

-- --------------------------------------------------------------
-- Create test data
-- --------------------------------------------------------------
insert into whitebox_db.manufacturer_table values (1, 'Intel');
insert into whitebox_db.manufacturer_table values (2, 'ASUS');
insert into whitebox_db.manufacturer_table values (3, 'Gigabyte');
insert into whitebox_db.manufacturer_table values (4, 'EVGA');

insert into whitebox_db.component_type_table values (1, 'Motherboard');
insert into whitebox_db.component_type_table values (2, 'Disk');
insert into whitebox_db.component_type_table values (3, 'USB Memory Stick');
insert into whitebox_db.component_type_table values (4, 'Mouse');
insert into whitebox_db.component_type_table values (5, 'Keyrboard');

insert into whitebox_db.customer_info values (
	default, 'Smith 1', 'North.Pole@yahoo.com', '1-905-111-1111', '10 Charles Street, Ottawa, ON, H0H-0H0', '2016-03-23');
insert into whitebox_db.customer_info values (
	default, 'Smith 2', 'North.Pole@yahoo.com', '1-905-222-2222', '10 Charles Street, Ottawa, ON, H0H-0H0', '2016-03-23');
insert into whitebox_db.customer_info values (
	default, 'Smith 3', 'North.Pole@yahoo.com', '', '10 Charles Street, Ottawa, ON, H0H-0H0', '2016-03-23');
 
insert into whitebox_db.payment_info values (1, 'Visa', 10000, 'www.abc.com/efg1.pdf');
insert into whitebox_db.payment_info values (2, 'Visa', 20000, 'www.abc.com/efg2.pdf');
insert into whitebox_db.payment_info values (3, 'Visa', 30000, 'www.abc.com/efg3.pdf');

  
insert into whitebox_db.product_info values (
	1, 1, 	-- `Build_ID`, `Component_type`
    1, 'Intel i7',	-- `Manufacturer`, `Product_description`
    3, 12345,	-- `model_number`, `Serial_number`
    0, 100,		-- `Rebate_value`, `Price`
    2, '2016-05-01',		-- `Warranty_period`, `Warranty_expire`
    '2016-05-01', 12345,	-- `Invoice_date`, `Invoice_number`
    2, 11111);				-- `Sale_order_number`, `Item_SKU`
insert into whitebox_db.product_info values (
	1, 2, 	-- `Build_ID`, `Component_type`
    2, 'Harddisk',	-- `Manufacturer`, `Product_description`
    3, 12345,	-- `model_number`, `Serial_number`
    0, 100,		-- `Rebate_value`, `Price`
    2, '2016-05-01',		-- `Warranty_period`, `Warranty_expire`
    '2016-05-01', 12345,	-- `Invoice_date`, `Invoice_number`
    2, 11111);				-- `Sale_order_number`, `Item_SKU`
insert into whitebox_db.product_info values (
	2, 1, 	-- `Build_ID`, `Component_type`
    1, 'Intel i7',	-- `Manufacturer`, `Product_description`
    3, 12345,	-- `model_number`, `Serial_number`
    0, 100,		-- `Rebate_value`, `Price`
    2, '2016-05-01',		-- `Warranty_period`, `Warranty_expire`
    '2016-05-01', 12345,	-- `Invoice_date`, `Invoice_number`
    2, 11111);				-- `Sale_order_number`, `Item_SKU`
insert into whitebox_db.product_info values (
	2, 2, 	-- `Build_ID`, `Component_type`
    2, 'Harddisk',	-- `Manufacturer`, `Product_description`
    3, 12345,	-- `model_number`, `Serial_number`
    0, 100,		-- `Rebate_value`, `Price`
    2, '2016-05-01',		-- `Warranty_period`, `Warranty_expire`
    '2016-05-01', 12345,	-- `Invoice_date`, `Invoice_number`
    2, 11111);	    
    
-- ------------------------------------------------------------------
-- Test script
-- ------------------------------------------------------------------   
SELECT * FROM whitebox_db.manufacturer_table;
SELECT * FROM whitebox_db.component_type_table;

-- Select from main tables
SELECT * FROM whitebox_db.customer_info;   
SELECT * FROM whitebox_db.customer_info WHERE Name LIKE 'Smi%1';	-- % is a wildcard replacing one or more characters

SELECT * FROM whitebox_db.payment_info;
SELECT * FROM whitebox_db.product_info;


-- Delete customer's build_id == 2. 
-- It will also delete all the payment_info and product_info that have build_id == 2
DELETE from whitebox_db.customer_info WHERE customer_info.Build_ID = 2;

SELECT * FROM whitebox_db.product_info;
-- set PINFO as shorthand of whitebox_db.product_info
-- set MANNAME as shorthand of whitebox_db.manufacturer_table.. .
SELECT PINFO.Build_ID, MANNAME.Manufacturer_name, COMP.Component_type_description, PINFO.Product_description 
FROM whitebox_db.product_info PINFO
JOIN whitebox_db.manufacturer_table MANNAME ON PINFO.Manufacturer = MANNAME.Manufacturer
JOIN whitebox_db.component_type_table COMP ON PINFO.Component_type = COMP.Component_type;


-- -------------------------------------------------------------
-- returns all rows with all coulums from all tables
SELECT *	
FROM whitebox_db.customer_info AS CINFO
JOIN whitebox_db.payment_info AS PYINFO ON CINFO.Build_ID = PYINFO.Build_ID
JOIN whitebox_db.product_info AS PRINFO ON CINFO.Build_ID = PRINFO.Build_ID
JOIN whitebox_db.manufacturer_table AS MANNAME ON PRINFO.Manufacturer = MANNAME.Manufacturer
JOIN whitebox_db.component_type_table AS COMP ON PRINFO.Component_type = COMP.Component_type
WHERE CINFO.Build_ID LIKE '1'
ORDER BY CINFO.Name;

-- Returns all rows with the prescribed coulumns from all tables
SELECT PYINFO.Payment_method_use, CINFO.Build_ID, MANNAME.Manufacturer_name, COMP.Component_type_description, PRINFO.Product_description 
FROM whitebox_db.customer_info AS CINFO
JOIN whitebox_db.payment_info AS PYINFO ON CINFO.Build_ID = PYINFO.Build_ID
JOIN whitebox_db.product_info AS PRINFO ON CINFO.Build_ID = PRINFO.Build_ID
JOIN whitebox_db.manufacturer_table AS MANNAME ON PRINFO.Manufacturer = MANNAME.Manufacturer
JOIN whitebox_db.component_type_table AS COMP ON PRINFO.Component_type = COMP.Component_type
ORDER BY CINFO.Build_ID;

-- Returns all rows with the input 'Build_ID' from all tables
SELECT CINFO.Name, PYINFO.Payment_method_use, CINFO.Build_ID, MANNAME.Manufacturer_name, COMP.Component_type_description, PRINFO.Product_description 
FROM whitebox_db.customer_info AS CINFO
JOIN whitebox_db.payment_info AS PYINFO ON CINFO.Build_ID = PYINFO.Build_ID
JOIN whitebox_db.product_info AS PRINFO ON CINFO.Build_ID = PRINFO.Build_ID
JOIN whitebox_db.manufacturer_table AS MANNAME ON PRINFO.Manufacturer = MANNAME.Manufacturer
JOIN whitebox_db.component_type_table AS COMP ON PRINFO.Component_type = COMP.Component_type
WHERE CINFO.name = 'smith 1'		-- Input is Build_ID = 2
ORDER BY PRINFO.Component_type;

-- Returns all rows with the input 'User Name' from all tables
SELECT CINFO.Name, PYINFO.Payment_method_use, CINFO.Build_ID, MANNAME.Manufacturer_name, COMP.Component_type_description, PRINFO.Product_description 
FROM whitebox_db.customer_info AS CINFO
JOIN whitebox_db.payment_info AS PYINFO ON CINFO.Build_ID = PYINFO.Build_ID
JOIN whitebox_db.product_info AS PRINFO ON CINFO.Build_ID = PRINFO.Build_ID
JOIN whitebox_db.manufacturer_table AS MANNAME ON PRINFO.Manufacturer = MANNAME.Manufacturer
JOIN whitebox_db.component_type_table AS COMP ON PRINFO.Component_type = COMP.Component_type
WHERE CINFO.Build_ID = (SELECT Build_ID FROM whitebox_db.customer_info WHERE whitebox_db.customer_info.Name LIKE 'Smith 1') -- Input is 'Smith 1'
ORDER BY PRINFO.Component_type;




-- Interface
-- User can execute the 'Select' statement to do the same thing. But using the Stored procedure is better because
-- 1) we can change the underlining tables without affecting the calling program.
-- Test stored procedure
call get_customer_by_name('Smith 1');
call get_customer_by_name('Smit%');	-- Uses wildcard. Returns every rows whose user name starts  with Smit. Case insensitive (set during install)

call get_customer_payment_by_name('Smith 2');
call get_customer_payment_by_name('Smith %');

call get_customer_product_by_name('Smith 2');
call get_customer_product_by_name('Smith 3');	-- He does not have anu order
call get_customer_product_by_name('Smith %');

call create_customer_by_name(
	'Smith 4', 
    'North.Pole@yahoo.com', 
    '1-905-111-4444', 
    '10 Charles Street, Ottawa, ON, H0H-0H0', 
    '2016-03-23'
);

call get_customer_by_name('Smit%');
call delete_customer_by_name('Smith 7');
call get_customer_by_name('Smit%');

call create_customer_by_name_with_payment_info(
	'Smith 7', 
    'North.Pole@yahoo.com', 
    '1-905-111-4444', 
    '10 Charles Street, Ottawa, ON, H0H-0H0', 
    '2016-03-23',
    
    'MasterCard', 
    30000, 
    'www.abc.com/efg3.pdf'
);


SELECT *
from customer_info AS CINFO
JOIN payment_info AS PINFO ON CINFO.Build_ID = PINFO.Build_ID
WHERE PINFO.Payment_method_use = 'Visa' and PINFO.Build_ID = '1' ;


SELECT * from customer_info AS CINFO
JOIN payment_info AS PINFO ON CINFO.Build_ID = PINFO.Build_ID
WHERE PINFO.Payment_method_use = 'Visa';