
public class Customer_Database_INFO {
	
	// can put in xml file later, but since we are implementing limited functionality that doesn't pertain to modifying the database itself
	// it is not necessary.
	
	public String[] Column_names = {
			// customer_info
			/* 0*/	"Build_ID",
			/* 1*/	"Name",
			/* 2*/	"Email",
			/* 3*/	"Phone",
			/* 4*/	"Address",
			/* 5*/	"Delivery_Date",
			
			// payment_info
			/* 6*/	"Build_ID",
			/* 7*/	"Payment_method_use",
			/* 8*/	"Total_value",
			/* 9*/	"Delivery_confirmation_link",
			
			// product_info
			/* 10*/	"Build_ID",
			/* 11*/	"Component_type",
			/* 12*/ "Manufacturer",
			/* 13*/	"Product_description",
			/* 14*/	"model_number",
			/* 15*/	"Serial_number",
			/* 16*/	"Rebate_value",
			/* 17*/	"Price",
			/* 18*/	"Warranty_period",
			/* 19*/	"Warranty_expire",
			/* 20*/	"Invoice_date",
			/* 21*/	"Invoice_number",
			/* 22*/	"Sale_order_number",
			/* 23*/	"Item_SKU",
			
			// manufacturer_table
			/* 24*/	"Manufacturer",
			/* 25*/	"Manufacturer_name",
			
			// component_type_table
			/* 26*/	"Component_type",
			/* 27*/	"Component_type_description"
			
	};
	
}
