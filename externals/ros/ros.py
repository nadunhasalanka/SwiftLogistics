from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from typing import List
import uuid
from datetime import datetime, timezone, timedelta
import logging
import asyncio
import uvicorn

# Setup logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

# Create the FastAPI app
app = FastAPI(
    title="ROS Mock Service",
    description="Mock Route Optimization System for SwiftLogistics",
    version="1.0.0"
)

# Corrected Order data model to match the Java Order object
class Order(BaseModel):
    id: int
    userId: str
    clientName: str
    packageDetails: str
    deliveryAddress: str
    status: str
    cmsStatus: str
    wmsStatus: str
    rosStatus: str

# Define other data models
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

# Simulate some processing delay
async def simulate_optimization_processing():
    """Simulate the time it takes to optimize routes"""
    await asyncio.sleep(1.5)
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
    Endpoint for adding individual delivery points with a simulated delay.
    """
    logger.info(f"Received order ID: {order.id}")
    logger.info("Simulating 3rd party integration delay...")
    # Simulate a 2-second integration delay for a complex system
    await asyncio.sleep(4)
    logger.info("3rd party integration complete.")
    logger.info(f"Client: {order.clientName}")
    logger.info(f"Delivery Address: {order.deliveryAddress}")

    return {
        "message": "Delivery point added successfully",
        "orderId": order.id,
        "status": "added"
    }

@app.get("/ros/routes/{route_id}")
async def get_route_status(route_id: str):
    """
    Get status of a specific route
    """
    await asyncio.sleep(0.2)
    return {
        "route_id": route_id,
        "status": "completed",
        "optimized_at": datetime.now(timezone.utc).isoformat(),
        "message": "Route optimization completed successfully"
    }

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8000)