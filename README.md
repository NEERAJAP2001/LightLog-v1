
# 🔍 Log Monitoring and Alerting System

A lightweight and modular Spring Boot-based log monitoring system designed for microservices running on Kubernetes/OpenShift. This tool collects pod logs, detects keywords like `error`, `exception`, and `shutdown`, and sends alert emails based on thresholds. A React-based dashboard displays live stats visually.

---

## 🚀 Features

- 📦 Polls logs from Kubernetes/OpenShift pods using Fabric8 Kubernetes API.
- 🧠 Detects custom keywords and tracks log trends per microservice.
- 📈 REST API to expose aggregated keyword counts.
- 📊 Interactive React dashboard with auto-refreshing charts (via Recharts).
- ✉️ Outlook-based email alerting with configurable thresholds.
- 🧩 Config-driven services, keywords, and thresholds — no redeploy needed.
- 🔒 Safe for production: thread-safe, concurrent, and avoids log duplication.

---

## 🛠️ Tech Stack

### Backend:
- Java 17
- Spring Boot 3+
- Fabric8 Kubernetes Client
- Outlook SMTP for alerting

### Frontend:
- React.js
- Axios
- Recharts

### Platform:
- OpenShift (Red Hat Developer Sandbox)

---

## ⚙️ Setup Instructions

### Backend (Spring Boot)
1. Clone the repo and update `application.properties`:
   ```
   logmonitor.services=log-producer-1,log-producer-2
   logmonitor.keywords=error,exception,shutdown
   alert.enabled=true
   alert.threshold.error=5
   ...
   ```

2. Run the app:
   ```
   mvn spring-boot:run
   ```

### Frontend (React)
```bash
cd log-monitor-ui
npm install
npm start
```

Make sure Spring Boot runs at `localhost:8080` or update API base URL in `LogChart.js`.

---

## 🌐 REST API

- `GET /api/logs/stats`  
Returns log keyword counts per service in JSON format.

---

## 🧱 System Architecture

```txt
+-----------------+        Kubernetes API         +-------------------------+
| Log Monitor App | --------------------------->  | OpenShift (Pods/Logs)   |
| (Spring Boot)   |                               +-------------------------+
|                 | REST API                      |
+--------+--------+                               |
         |                                        |
         | JSON                                   |
         v                                        |
+--------+--------+                               |
| React Dashboard | <-----------------------------+
| (Recharts, Axios)                             
+-----------------+
```

---

## 🚨 Alerting

- Configurable via `application.properties`.
- Uses Outlook SMTP.
- One alert per threshold breach (prevent alert spam).

---

## 📦 Directory Structure

```
log-monitoring/
├── src/main/java/com/openshift/logmonitoring/
│   ├── controller/
│   ├── service/
│   ├── model/
│   └── config/
├── resources/application.properties
└── README.md
```

---

## 🛣️ Future Plans (v2+)

- 🔔 Slack or Microsoft Teams alerts
- 🧪 Prometheus + Grafana integration
- ⏱️ Live log streaming with WebSockets
- 📦 Docker + Helm chart deployment
- 🧠 ML-based anomaly detection for logs
- 📝 Web UI for managing monitored keywords & alert rules

---

## 📄 License

MIT License
