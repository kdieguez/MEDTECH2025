from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from routers import estructura_web, autenticacion, usuarios

app = FastAPI()

origins = [
    "http://localhost:5173", "*"
]
app.add_middleware(
    CORSMiddleware,
    allow_origins=origins,  
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# routers
app.include_router(estructura_web.router)
app.include_router(autenticacion.router)
app.include_router(usuarios.router)

@app.get("/")
def read_root():
    return {"message": "El backend funciona"}
