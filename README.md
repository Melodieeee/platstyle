
# [PlatStyle ✂️](https://github.com/Melodieeee/platstyle)

**PlatStyle** is a web platform for hair stylists to showcase their work and for users to book appointments with them.

Ever struggled to find the perfect haircut in Vancouver? Between navigating busy schedules, skyrocketing prices, and limited stylist information, getting a fresh cut can feel like a chore.  
That’s where **PlatStyle** comes in.

PlatStyle is your one-stop solution to connect with talented hair stylists across the Vancouver metropolitan area. With our modern and user-friendly web app, finding your next haircut is a breeze.  
Ditch the stress—embrace a world of convenient haircuts with PlatStyle!

---

## 🚀 Live Demo
[https://platstyle.onrender.com](https://platstyle.onrender.com)  
*Deployed on Render.com, using PostgreSQL as the production database.*

---

## 👩‍💻 Project Contributors
- [Melody Yu](https://github.com/Melodieeee)
- [Sam Yang](https://github.com/yangsam810)
- Cindy Cheng

---

## 🛠 Technologies Used
- **Spring Boot (Java 17)**
- **Thymeleaf**
- **PostgreSQL (Render-hosted)**
- **Spring Data JPA**
- **Bootstrap 5**
- **Maven**
- Docker (for deployment)
- RESTful API (for AJAX messages)
- Role-based authentication with Spring Security

> 📆 Originally built in **2022**  
> 🔁 Re-deployed to **Render in 2025** with Docker and remote PostgreSQL

---

## 🧠 Project Overview & Design

- [🧾 Presentation Slides](https://drive.google.com/file/d/1IwFl_pzz91TRSMje6nYe5vPOb6s-oW2g/view?usp=drive_link)
- [📈 Development Process](https://drive.google.com/file/d/1oFFzJpvxwpr-1g9H8S6yAxyiCOR_fKdW/view?usp=drive_link)

---

## 📁 Project Structure

| Component | File / Path |
|----------|-------------|
| Main Class | [`PlatStyleApplication`](src/main/java/com/example/platstyle/PlatStyleApplication.java) |
| Config File | [`application.properties`](src/main/resources/application.properties) |

---

## 🧪 Test Accounts

| Role   | Email                | Password |
|--------|----------------------|----------|
| Admin  | admin@gmail.com      | admin    |
| Stylist (unverified) | stylist@gmail.com    | stylist  |
| Stylist (verified)   | stylist2@gmail.com   | stylist  |
| User   | appUser@gmail.com    | user     |

---

## 🖥️ Run Locally

### 1. Clone the project

```bash
git clone https://github.com/Melodieeee/platstyle.git
cd platstyle
```

### 2. Configure database
Edit [`application.properties`](src/main/resources/application.properties) or use environment variables:

#### Example `.env` file
```
psql_DATABASE_URL=jdbc:postgresql://<your-host>/<your-db>
psql_DATABASE_USERNAME=<your-username>
psql_DATABASE_PASSWORD=<your-password>
```

> The database config can be easily switched for **Heroku**, **GCP**, **MySQL**, or **PostgreSQL** by editing `application.properties`.

### 3. Run the app
```bash
./mvnw spring-boot:run
# or if you use IDE: run PlatStyleApplication.java
```

### 4. Visit
[http://localhost:8080](http://localhost:8080)

---

## 🐳 Docker (Local Test)

### Build and run the app

```bash
docker build -t platstyle-app .
docker run -p 8080:8080 --env-file .env platstyle-app
```

---

## 📦 Deployment (Render.com)

PlatStyle is deployed using a Docker container and connects to a managed PostgreSQL database via Render.

Steps:
1. Create new **Web Service** using Dockerfile
2. Add environment variables for DB credentials
3. Connect to Render-hosted PostgreSQL

---

## 📎 License

MIT License © 2022 PlatStyle, Inc
