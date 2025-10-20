from fastapi import FastAPI, HTTPException, Request
from fastapi.responses import Response, JSONResponse
import xml.etree.ElementTree as ET
import uuid
from datetime import datetime
from typing import Dict, List
import logging

# Setup logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

app = FastAPI(
    title="Mock CMS SOAP Service",
    description="Mock Client Management System for SwiftLogistics",
    version="1.0.0"
)

# In-memory storage for demonstration
clients = {
    "client_001": {
        "name": "ABC Enterprises", 
        "contract_type": "Premium",
        "billing_address": "123 Main St, Colombo",
        "contact_email": "billing@abcenterprises.lk"
    },
    "client_002": {
        "name": "XYZ Retail", 
        "contract_type": "Standard",
        "billing_address": "456 Galle Rd, Colombo",
        "contact_email": "accounts@xyzretail.lk"
    }
}

orders = {}
order_counter = 1000

@app.get("/")
async def root():
    return {"message": "CMS Mock Service is running!"}

@app.get("/health")
async def health_check():
    return {"status": "healthy", "service": "CMS"}

@app.post("/cms/soap-api")
async def handle_soap_request(request: Request):
    """Handle SOAP requests to the CMS"""
    try:
        # Get the SOAP request body
        soap_body = await request.body()
        soap_xml = soap_body.decode('utf-8')
        
        logger.info(f"Received SOAP request: {soap_xml}")
        
        # Parse the SOAP request
        try:
            root = ET.fromstring(soap_xml)
        except ET.ParseError:
            return JSONResponse(
                status_code=400,
                content={"error": "Invalid XML format"}
            )
        
        # Extract the SOAP Body content
        namespace = {'soap': 'http://schemas.xmlsoap.org/soap/envelope/'}
        body = root.find('.//soap:Body', namespace)
        
        if body is None:
            raise HTTPException(status_code=400, detail="Invalid SOAP request")
        
        # Check which operation is being requested
        operation = None
        for child in body:
            operation = child.tag
            # Remove namespace if present
            if '}' in operation:
                operation = operation.split('}')[1]
            break
        
        if not operation:
            raise HTTPException(status_code=400, detail="No operation specified")
        
        # Handle different SOAP operations
        if "submitOrder" in operation:
            return handle_submit_order(body)
        elif "getClientInfo" in operation:
            return handle_get_client_info(body)
        elif "getOrderStatus" in operation:
            return handle_get_order_status(body)
        elif "createClient" in operation:
            return handle_create_client(body)
        else:
            raise HTTPException(status_code=400, detail=f"Unknown operation: {operation}")
            
    except Exception as e:
        logger.error(f"SOAP processing error: {str(e)}")
        raise HTTPException(status_code=500, detail=f"SOAP processing error: {str(e)}")

def handle_submit_order(body):
    """Handle order submission request"""
    global order_counter
    
    # Extract order details from SOAP body
    order_elem = body.find('.//orderDetails')
    if order_elem is None:
        raise HTTPException(status_code=400, detail="No order details provided")
    
    client_id_elem = order_elem.find('clientId')
    items_elem = order_elem.find('items')
    priority_elem = order_elem.find('priority')
    
    if client_id_elem is None or items_elem is None:
        raise HTTPException(status_code=400, detail="Missing required order fields")
    
    client_id = client_id_elem.text
    if client_id not in clients:
        raise HTTPException(status_code=404, detail="Client not found")
    
    # Extract items
    items = []
    for item_elem in items_elem.findall('item'):
        items.append({
            "id": item_elem.find('id').text if item_elem.find('id') is not None else str(uuid.uuid4())[:8],
            "description": item_elem.find('description').text if item_elem.find('description') is not None else "Unknown product",
            "quantity": int(item_elem.find('quantity').text) if item_elem.find('quantity') is not None else 1
        })
    
    # Generate order ID
    order_id = f"ORD_{order_counter}"
    order_counter += 1
    
    # Store order (in memory for demo)
    orders[order_id] = {
        "client_id": client_id,
        "status": "received",
        "items": items,
        "priority": priority_elem.text if priority_elem is not None else "standard",
        "created_at": datetime.now().isoformat()
    }
    
    logger.info(f"Created new order: {order_id} for client: {client_id}")
    
    # Create SOAP response
    soap_response = f"""<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
  <soap:Body>
    <submitOrderResponse xmlns="http://example.com/cms/soap">
      <orderId>{order_id}</orderId>
      <status>success</status>
      <message>Order received successfully</message>
      <timestamp>{datetime.now().isoformat()}</timestamp>
    </submitOrderResponse>
  </soap:Body>
</soap:Envelope>"""
    
    return Response(content=soap_response, media_type="application/xml")

def handle_get_client_info(body):
    """Handle client information request"""
    client_id_elem = body.find('.//clientId')
    if client_id_elem is None:
        raise HTTPException(status_code=400, detail="No client ID provided")
    
    client_id = client_id_elem.text
    if client_id not in clients:
        raise HTTPException(status_code=404, detail="Client not found")
    
    client_info = clients[client_id]
    
    # Create SOAP response
    soap_response = f"""<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
  <soap:Body>
    <getClientInfoResponse xmlns="http://example.com/cms/soap">
      <clientId>{client_id}</clientId>
      <name>{client_info['name']}</name>
      <contractType>{client_info['contract_type']}</contractType>
      <billingAddress>{client_info['billing_address']}</billingAddress>
      <contactEmail>{client_info['contact_email']}</contactEmail>
    </getClientInfoResponse>
  </soap:Body>
</soap:Envelope>"""
    
    return Response(content=soap_response, media_type="application/xml")

def handle_get_order_status(body):
    """Handle order status request"""
    order_id_elem = body.find('.//orderId')
    if order_id_elem is None:
        raise HTTPException(status_code=400, detail="No order ID provided")
    
    order_id = order_id_elem.text
    if order_id not in orders:
        raise HTTPException(status_code=404, detail="Order not found")
    
    order_info = orders[order_id]
    
    # Create SOAP response
    soap_response = f"""<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
  <soap:Body>
    <getOrderStatusResponse xmlns="http://example.com/cms/soap">
      <orderId>{order_id}</orderId>
      <status>{order_info['status']}</status>
      <clientId>{order_info['client_id']}</clientId>
      <priority>{order_info['priority']}</priority>
      <createdAt>{order_info['created_at']}</createdAt>
    </getOrderStatusResponse>
  </soap:Body>
</soap:Envelope>"""
    
    return Response(content=soap_response, media_type="application/xml")

def handle_create_client(body):
    """Handle client creation request"""
    client_elem = body.find('.//clientDetails')
    if client_elem is None:
        raise HTTPException(status_code=400, detail="No client details provided")
    
    name_elem = client_elem.find('name')
    contract_type_elem = client_elem.find('contractType')
    
    if name_elem is None or contract_type_elem is None:
        raise HTTPException(status_code=400, detail="Missing required client fields")
    
    # Generate client ID
    client_id = f"client_{str(uuid.uuid4())[:8]}"
    
    # Store client (in memory for demo)
    clients[client_id] = {
        "name": name_elem.text,
        "contract_type": contract_type_elem.text,
        "billing_address": client_elem.find('billingAddress').text if client_elem.find('billingAddress') is not None else "",
        "contact_email": client_elem.find('contactEmail').text if client_elem.find('contactEmail') is not None else ""
    }
    
    logger.info(f"Created new client: {client_id} - {name_elem.text}")
    
    # Create SOAP response
    soap_response = f"""<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
  <soap:Body>
    <createClientResponse xmlns="http://example.com/cms/soap">
      <clientId>{client_id}</clientId>
      <status>success</status>
      <message>Client created successfully</message>
    </createClientResponse>
  </soap:Body>
</soap:Envelope>"""
    
    return Response(content=soap_response, media_type="application/xml")

# REST endpoint for testing (not part of the SOAP API)
@app.get("/cms/clients")
async def get_clients():
    """Get all clients (REST endpoint for testing)"""
    return clients

@app.get("/cms/orders")
async def get_orders():
    """Get all orders (REST endpoint for testing)"""
    return orders

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="127.0.0.1", port=8001)