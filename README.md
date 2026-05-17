# DIP Object Detection Project

This project was developed as part of the Digital Image Processing (DIP) course.

The goal of the project is to detect a query object inside a larger scene image using classical image processing techniques.

---

# Project Overview

The system detects objects inside scene images using multiple classical computer vision methods and compares their performance under different conditions.

## Implemented Detection Methods

- Template Matching (NCC)
- Edge-Based Detection
- Histogram Matching
- Hybrid Detection Method

The project also includes robustness evaluation under different transformations such as:

- Rotation
- Noise
- Brightness Variation
- Scale Change

---

# Dataset

The project uses two scene images:

- Notebook Scene
- USB Scene

Each scene contains a query object image cropped from the original scene.

---

# Project Structure

```text
dip_object_detection_project_v3/
│
├── data/
│   ├── scene_book.png
│   ├── scene_usb.png
│   ├── object_book_o.png
│   ├── object_usb_o.png
│   └── ...
│
├── outputs/
│   ├── robustness/
│   │   ├── rotation/
│   │   ├── noise/
│   │   ├── brightness/
│   │   └── scale/
│   └── results_summary.csv
│
├── src/
│   ├── config.py
│   ├── preprocessing.py
│   ├── detection_methods.py
│   ├── evaluation.py
│   └── experiment.py
│
├── main.py
└── README.md
```

---

# Requirements

Install the required libraries:

```bash
pip install opencv-python numpy pandas matplotlib
```

---

# Run the Project

To run all experiments:

```bash
python main.py
```

---

# Output

The program generates:

- Detected object images with bounding boxes
- IoU scores
- Detection confidence scores
- Execution time comparison
- Robustness test outputs
- CSV summary file

Main outputs are saved inside:

```text
outputs/
```

Robustness outputs are saved inside:

```text
outputs/robustness/
```

---

# Evaluation Metrics

The methods were evaluated using:

- Intersection over Union (IoU)
- Detection Success
- Computational Time
- Robustness Against Transformations

---

# Hybrid Method

The proposed hybrid method combines multiple image processing techniques including:

- Preprocessing
- HSV Color Segmentation
- Morphological Operations
- Contour Filtering
- Localization Refinement

The hybrid method achieved the best overall performance among all implemented approaches.

---

# Author

Digital Image Processing Project

Developed by:

**Hamdi Balati**
