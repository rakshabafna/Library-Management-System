# 📚 Library Management System (Java Swing Desktop App)

A robust and user-friendly desktop application built using Java Swing for managing books, members, and circulation workflows in small-to-medium-sized libraries. Designed for librarians and admins, this system ensures efficient tracking, member handling, overdue management, and data-driven insights through charts.

---

## 🎯 Features

- 🔐 **User Login & Registration**
- 📚 **Book Inventory Management**
  - Add / Update / Delete / View books
- 👥 **Member Management**
  - Add / Modify / Remove member records
- 🔄 **Book Issue & Return**
  - Track issue/return dates
  - Auto-calculate fines for overdue books
- 📅 **Real-time Due Date Monitoring**
- 📊 **Dashboard with Charts**
  - Visualize data like most issued books, active members, overdue trends
- 🧾 **Email Format Validation** & Input Constraints
- 🪪 **Unique Member ID & Book ID Management**

---

## 📈 Chart Integration

- Integrated **Java-based charting library** (e.g., JFreeChart or custom Swing plots)
- Live graphical summaries such as:
  - 🔝 Top 5 Most Issued Books
  - 🧍‍♂️ Active vs Inactive Members
  - 🕒 Issue vs Return Trends (Daily/Monthly)
  - 💰 Fines Collected Over Time

These charts help librarians make informed decisions and improve library resource planning.

---

## 🧰 Built With

| Component      | Technology            |
|----------------|------------------------|
| UI             | Java Swing             |
| IDE            | NetBeans               |
| Database       | Microsoft SQL Server   |
| DB Access      | JDBC                   |
| Charts         | JFreeChart (or similar)|
| Language       | Java (JDK 17+)         |

---

## 💻 How to Run the Application

### 🧱 Prerequisites

- Java JDK 17 or higher
- NetBeans IDE 15 or later (with Swing GUI Builder)
- SQL Server (Local or Remote)
- JDBC SQL Server Driver

### 🛠️ Setup Instructions

1. **Clone the repository**
   ```bash
   git clone https://github.com/your-username/library-management-system-desktop.git
