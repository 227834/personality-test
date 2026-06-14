# 個性測驗系統

一個基於 Java Spring Boot、MySQL 和 MVC 架構的完整個性測驗網頁應用。

## 功能特性

✅ **會員系統**
- 用戶註冊（驗證用戶名、郵箱、密碼）
- 用戶登錄（BCrypt 密碼加密）
- Session 管理
- 登出功能

✅ **個性測驗**
- 5 題個性測試問卷
- 4 個選項 (A、B、C、D)
- 根據回答計算個性類型
- 分數計算系統

✅ **數據管理**
- MySQL 數據庫存儲
- 用戶表（users）
- 測驗問題表（questions）
- 測驗結果表（test_results）

✅ **前端界面**
- 純 HTML、CSS、JavaScript 實現
- 響應式設計
- AJAX 前後端通信
- 實時錯誤提示

## 技術棧

**後端**
- Java 17
- Spring Boot 3.1.5
- Spring Data JPA
- Spring Security (BCrypt)
- MySQL 8.0

**前端**
- HTML5
- CSS3
- JavaScript (Vanilla)
- AJAX

**數據庫**
- MySQL 8.0+

## 項目結構

```
personality-test/
├── pom.xml                                  # Maven 配置
├── src/
│   └── main/
│       ├── java/com/personalitytest/
│       │   ├── PersonalityTestApplication.java      # 主應用程序
│       │   ├── model/                               # 數據模型
│       │   │   ├── User.java
│       │   │   ├── Question.java
│       │   │   └── TestResult.java
│       │   ├── repository/                          # 數據庫操作層
│       │   │   ├── UserRepository.java
│       │   │   ├── QuestionRepository.java
│       │   │   └── TestResultRepository.java
│       │   ├── service/                             # 業務邏輯層
│       │   │   ├── UserService.java
│       │   │   ├── QuestionService.java
│       │   │   └── TestResultService.java
│       │   ├── controller/                          # 控制層
│       │   │   ├── AuthController.java
│       │   │   ├── QuestionController.java
│       │   │   ├── TestController.java
│       │   │   └── PageController.java
│       │   ├── config/
│       │   │   └── SecurityConfig.java
│       │   └── listener/
│       │       └── ApplicationStartupListener.java
│       └── resources/
│           ├── application.properties                # 應用配置
│           └── static/                              # 靜態文件
│               ├── index.html
│               ├── css/style.css
│               └── js/app.js
└── README.md
```

## 安裝指南

### 1. 克隆項目
```bash
git clone https://github.com/227834/personality-test.git
cd personality-test
```

### 2. 配置 MySQL 數據庫
```sql
-- 創建數據庫
CREATE DATABASE personality_test CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 使用數據庫
USE personality_test;
```

### 3. 更新數據庫配置
編輯 `src/main/resources/application.properties`：
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/personality_test
spring.datasource.username=root
spring.datasource.password=your_password
```

### 4. 構建項目
```bash
mvn clean install
```

### 5. 運行應用
```bash
mvn spring-boot:run
```

應用將在 `http://localhost:8080` 啟動。

## 使用指南

### 1. 註冊新用戶
- 點擊「註冊」鏈接
- 填寫用戶名、郵箱和密碼
- 確認密碼
- 點擊「註冊」按鈕

### 2. 登錄
- 輸入用戶名和密碼
- 點擊「登入」按鈕

### 3. 進行測驗
- 登錄後自動進入測驗頁面
- 回答所有 5 個問題
- 點擊「提交測驗」按鈕

### 4. 查看結果
- 系統會顯示個性類型
- 包含分數和詳細描述
- 可以重新測驗或查看歷史記錄

## 數據庫設計

### Users 表
```sql
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    created_at BIGINT
);
```

### Questions 表
```sql
CREATE TABLE questions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    question_text VARCHAR(255) NOT NULL,
    option_a VARCHAR(255) NOT NULL,
    option_b VARCHAR(255) NOT NULL,
    option_c VARCHAR(255) NOT NULL,
    option_d VARCHAR(255) NOT NULL,
    question_number INT NOT NULL,
    personality_type VARCHAR(50) NOT NULL
);
```

### Test_Results 表
```sql
CREATE TABLE test_results (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    personality_type VARCHAR(100) NOT NULL,
    personality_description TEXT NOT NULL,
    score INT NOT NULL,
    test_date BIGINT,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
```

## API 端點

### 認證 API
- `POST /api/auth/register` - 用戶註冊
- `POST /api/auth/login` - 用戶登錄
- `POST /api/auth/logout` - 用戶登出
- `GET /api/auth/check-session` - 檢查 Session

### 問題 API
- `GET /api/questions` - 獲取所有問題
- `POST /api/questions/init` - 初始化問題

### 測驗 API
- `POST /api/test/submit` - 提交測驗
- `GET /api/test/history` - 獲取測驗歷史

## 個性類型分類

根據分數，系統會分類為：

- **外向樂觀理性冒險者** (分數 ≥ 10)
- **平衡全能型** (分數 5-9)
- **謹慎穩定型** (分數 0-4)
- **內向保守型** (分數 < 0)

## 安全性

✅ BCrypt 密碼加密
✅ 用戶名和郵箱唯一性驗證
✅ Session 管理
✅ CORS 配置
✅ 輸入驗證

## 許可證

MIT License

## 聯系

如有問題或建議，請提交 Issue 或 Pull Request。
