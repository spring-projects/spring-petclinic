"""
Spring PetClinic AI Service - Pet Breed Classifier API
"""
import os
import hashlib
from fastapi import FastAPI, HTTPException

app = FastAPI(title="PetClinic Breed Classifier", version="1.0.0")

MODEL_PATH = os.path.join(os.path.dirname(__file__), "../models/classifier.onnx")

@app.get("/health")
def health():
    if not os.path.exists(MODEL_PATH):
        raise HTTPException(status_code=500, detail="Model checkpoint not found")
    with open(MODEL_PATH, "rb") as f:
        digest = hashlib.sha256(f.read()).hexdigest()
    return {"status": "healthy", "model_sha256": digest}

@app.post("/predict")
def predict_breed():
    # Stub inference returning top simulated prediction
    return {
        "breed": "Golden Retriever",
        "confidence": 0.94,
        "model_version": "1.0.0"
    }
