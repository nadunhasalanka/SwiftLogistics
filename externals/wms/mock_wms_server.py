import socket

HOST = "127.0.0.1"
PORT = 9090

with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
    s.bind((HOST, PORT))
    s.listen()
    print(f"Mock WMS server listening on {HOST}:{PORT}")
    while True:
        conn, addr = s.accept()
        with conn:
            print(f"Connected by {addr}")
            data = conn.recv(1024)
            if not data:
                break
            message = data.decode("utf-8").strip()
            # Simple response logic
            if message.startswith("GET_STATUS"):
                response = "STATUS_OK"
            elif message.startswith("LOAD"):
                response = "LOAD_OK"
            elif message.startswith("RECEIVE"):
                response = "RECEIVE_OK"
            else:
                response = "UNKNOWN_COMMAND"
            conn.sendall(response.encode("utf-8"))