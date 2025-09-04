from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from typing import List
import uuid
from datetime import datetime, timezone, timedelta
import logging
import asyncio  # Added for async compatibility

# Setup logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

# Create the FastAPI app
app = FastAPI(
    title="ROS Mock Service",
    description="Mock Route Optimization System for SwiftLogistics",
    version="1.0.0"
)

# Define data models
class Order(BaseModel):
    orderId: str
    address: str

class OptimizationRequest(BaseModel):
    vehicle_id: str
    orders: List[Order]

class OptimizedStop(BaseModel):
    stop_number: int
    order_id: str
    address: str
    estimated_arrival: str

class OptimizationResponse(BaseModel):
    optimized_route_id: str
    vehicle_id: str
    optimized_sequence: List[OptimizedStop]
    total_estimated_duration: str
    total_distance: str

# Simulate some processing delay like a real ROS would have
async def simulate_optimization_processing():
    """Simulate the time it takes to optimize routes"""
    await asyncio.sleep(1.5)  # 1.5 second delay
    logger.info("Route optimization processing completed")

@app.get("/")
async def root():
    return {"message": "ROS Mock Service is running!"}

@app.get("/health")
async def health_check():
    return {"status": "healthy", "service": "ROS"}

@app.post("/ros/delivery-points")
async def add_delivery_points(order: Order):
    """
    Endpoint for adding individual delivery points
    """
    logger.info(f"Added delivery point for order: {order.orderId}")
    logger.info(f"Address: {order.address}")
    
    return {
        "message": "Delivery point added successfully",
        "orderId": order.orderId,
        "status": "added"
    }

@app.post("/ros/routes/optimize")
async def optimize_routes(request: OptimizationRequest):
    """
    Main endpoint that simulates route optimization
    """
    logger.info(f"Received optimization request for vehicle: {request.vehicle_id}")
    logger.info(f"Number of orders: {len(request.orders)}")
    
    # Simulate processing time (like a real optimization algorithm)
    await simulate_optimization_processing()
    
    # Generate unique route ID
    route_id = f"route-{datetime.now().strftime('%Y%m%d')}-{str(uuid.uuid4())[:8]}"
    
    # Create hard-coded optimized route
    optimized_sequence = []
    for i, order in enumerate(request.orders):
        # Calculate fake arrival time (30 minutes per stop)
        eta = datetime.now(timezone.utc) + timedelta(minutes=30 * (i + 1))
        
        optimized_sequence.append(OptimizedStop(
            stop_number=i + 1,
            order_id=order.orderId,
            address=order.address,
            estimated_arrival=eta.isoformat()
        ))
    
    # Build response
    response = OptimizationResponse(
        optimized_route_id=route_id,
        vehicle_id=request.vehicle_id,
        optimized_sequence=optimized_sequence,
        total_estimated_duration=f"{len(request.orders) * 30} minutes",
        total_distance=f"{len(request.orders) * 12.5:.1f} km"
    )
    
    logger.info(f"Generated optimized route: {route_id}")
    return response

@app.get("/ros/routes/{route_id}")
async def get_route_status(route_id: str):
    """
    Get status of a specific route
    """
    # Simulate a small delay for database lookup
    await asyncio.sleep(0.2)
    
    return {
        "route_id": route_id,
        "status": "completed",
        "optimized_at": datetime.now(timezone.utc).isoformat(),
        "message": "Route optimization completed successfully"
    }

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8000)