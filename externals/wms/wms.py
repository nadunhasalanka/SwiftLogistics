from fastapi import FastAPI
import logging
import uvicorn
import asyncio

# Setup logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

# Create the FastAPI app
app = FastAPI(
    title="WMS Mock Service",
    description="Mock Warehouse Management System for SwiftLogistics",
    version="1.0.0"
)

@app.post("/packages/{package_id}/receive")
async def receive_package(package_id: str):
    """Marks a package as received from a client in WMS with a simulated delay."""
    logger.info(f"WMS received a request to receive package with ID: {package_id}")
    # Simulate a 1-second legacy system processing delay
    logger.info("Simulating legacy system delay...")
    await asyncio.sleep(7)
    logger.info("Legacy system processing complete.")
    return {"package_id": package_id, "result": "received"}

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=9090)