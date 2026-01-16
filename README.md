# Inventory Management Service

## 📄 Project Overview
This project is a Spring Boot-based inventory management service designed to handle product inventory with batch-level tracking, particularly focusing on expiry date-based operations. It supports fetching inventory details and updating stock levels using strategies like FIFO (First In, First Out).

---

## 🧩 Key Features
- **Product Inventory Fetch**: Retrieve product inventory details with batch-level information (quantity, expiry date) ordered by expiry.
- **Dynamic Inventory Update**: Adjust inventory quantities either:
    - **Externally** (e.g., sales): Reduces quantity starting from the earliest expiring batches.
    - **Internally** (e.g., restocking): Sets quantity for all batches uniformly.

---

## 🛠️ Prerequisites
- **Java 17+**
- **Spring Boot 3.x**
- **Maven** for dependency management

---



## 📦 API Endpoints

### 1. **Get Product Inventory**
**Endpoint**: `GET /inventory/{productId}`  
**Description**: Fetch inventory details for a product, ordered by expiry date.  
**Response**:
```json
{
    "productId": 1003,
    "productName": "Tablet",
    "batches": [
        {
            "batchId": 4,
            "quantity": 20,
            "expiryDate": "2026-09-03"
        },
        {
            "batchId": 8,
            "quantity": 20,
            "expiryDate": "2026-09-09"
        }
    ]
}
```

---

### 2. **Update Inventory Quantity**
**Endpoint**: `POST /inventory/update`  
**Request Body**:
```json
{
    "productId": 1003,
    "quantity": 31
}
```
**Header**
fromOutSide : false

**Response Body**:
{
    "productId": 1003,
    "productName": "Tablet",
    "batches": [
        {
            "batchId": 4,
            "quantity": 31,
            "expiryDate": "2026-09-03"
        },
        {
            "batchId": 8,
            "quantity": 31,
            "expiryDate": "2026-09-09"
        }
    ]
}

**Description**:
- If `fromOutside=true`: Reduces 31 units from the earliest expiring batches.
- If `fromOutside=false`: Sets all batches to 31 units (e.g., for restocking).

**Test Cases**
I have written JUnit test cases via Mockito