# Chrome Download Scanner Extension

A Chrome extension that **intercepts file downloads**, sends a small portion of the file to a backend API for scanning, and decides whether to allow or block the download based on the response. Designed as a **demonstration of browser extension development with backend integration**.

---

## 🧩 Features

- Intercepts all file downloads in Chrome.  
- Cancels the original download and sends the first 16 bytes to a backend API.  
- Backend API checks the file type based on its magic number and returns true to block or false to allow
- Blocks the download if the API detects a suspicious file.  
- Re-triggers the download if allowed.  
- Built-in mechanism to prevent infinite download loops.  
- Lightweight and safe, with fail-safe behavior on errors.  

---

## 🛠️ Technologies Used

- **Frontend:** JavaScript, Chrome Extension APIs (`downloads`, `runtime`)  
- **Backend:** Java, Spring Boot  
- **Build Tools:** Maven  
- **Testing:** JUnit (unit & integration tests)  

---

## 🧪 How It Works

1. Chrome starts a download → `chrome.downloads.onCreated` triggers.  
2. The extension cancels the download temporarily.  
3. Fetches the **first 16 bytes** of the file via `fetch` with a `Range` request.  
4. Sends the bytes as a `FormData` file to the backend API (`/file-monitor/should-block`).  
5. Backend service checks the bytes against **blocked file magic numbers** and returns whatever it should be blocked or not. 
6. If the file is allowed, the extension re-triggers the download.  
7. If there is **any error** (network, fetch, or backend), the download is **blocked** by default.  

---

## ⚙️ Setup

### Backend (Java / Spring Boot)

1. Navigate to `FileDownloadBlocker/FileDownloadBlocker-api/`  
2. Build and run:
```bash
mvn clean install
java -jar target/FileDownloadBlocker-api-0.0.1-SNAPSHOT.jar
3. The API runs locally on port 8081.
```

### Frontend (Chrome Extension)
 
 1. Open Chrome and go to: chrome://extensions
 2. Enable Developer mode.
 3. Click Load unpacked and select the folder: extension
 4. Downloads will now be intercepted automatically.

 ---

 ### 🔒 Fail-safe Design
 - Any backend or network error results in the file being blocked.
 - Only small portions of the file (first 16 bytes) are sent for scanning → fast and lightweight.
 - restartedDownloads prevents infinite re-download loops.

 ### 🧪 Customization
 - Blocked file types are defined in application.properties (backend) as hex magic numbers.
 - You can modify them to detect additional file types.