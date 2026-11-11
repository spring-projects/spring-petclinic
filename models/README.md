---
language:
- en
license: apache-2.0
tags:
- vision
- image-classification
- vit
datasets:
- stanford-dogs-v1
- oxford-iiit-pet
metrics:
- accuracy
pipeline_tag: image-classification
base_model: google/vit-base-patch16-224
---

# Pet Breed Classification Model

## Model Details
* **Architecture:** Vision Transformer (ViT-B/16) exported to ONNX Runtime
* **Task:** Multi-class classification (Dog & Cat Breeds)
* **Intended Use:** Assist intake desk staff with automated breed identification in Spring PetClinic.
* **Limitations:** Not designed for clinical diagnosis or pedigree verification.
