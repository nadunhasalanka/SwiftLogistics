from fastapi import FastAPI, HTTPException
import asyncio
import socket

app = FastAPI(title="WMS REST Adapter")

# WMS connection details (example values)
WMS_HOST = "127.0.0.1"
WMS_PORT = 9090

async def send_wms_message(message: str) -> str:
    """Send a message to WMS over TCP/IP and get the response."""
    try:
        reader, writer = await asyncio.open_connection(WMS_HOST, WMS_PORT)

        # Send message
        writer.write(message.encode("utf-8") + b"\n")
        await writer.drain()

        # Receive response
        data = await reader.read(4096)
        response = data.decode("utf-8").strip()

        writer.close()
        await writer.wait_closed()

        return response
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"WMS communication failed: {e}")


@app.get("/packages/{package_id}")
async def get_package_status(package_id: str):
    """Check the status of a package in WMS."""
    message = f"GET_STATUS {package_id}"
    response = await send_wms_message(message)
    return {"package_id": package_id, "status": response}


@app.post("/packages/{package_id}/load")
async def load_package(package_id: str, vehicle_id: str):
    """Mark a package as loaded onto a vehicle in WMS."""
    message = f"LOAD {package_id} VEHICLE {vehicle_id}"
    response = await send_wms_message(message)
    return {"package_id": package_id, "vehicle_id": vehicle_id, "result": response}


@app.post("/packages/{package_id}/receive")
async def receive_package(package_id: str, client_id: str):
    """Mark a package as received from a client in WMS."""
    message = f"RECEIVE {package_id} CLIENT {client_id}"
    response = await send_wms_message(message)
    return {"package_id": package_id, "client_id": client_id, "result": response}
