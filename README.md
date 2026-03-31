# VendorVault CLM - Contract Lifecycle Management System

## Overview

VendorVault CLM is an enterprise-grade Contract Lifecycle Management system built for Orion Heavy Industries Ltd. It provides comprehensive vendor management, contract tracking, approval workflows, and compliance monitoring capabilities.

## Tech Stack

- **Backend Framework**: Spring Boot 3.3.3
- **Java Version**: 17
- **Database**: MySQL 8.0
- **ORM**: Hibernate/JPA
- **Database Migration**: Flyway
- **Template Engine**: Thymeleaf 3.x
- **Security**: Spring Security 6
- **Build Tool**: Maven 3.9+
- **Caching**: Caffeine
- **PDF Generation**: iText 7.2.5
- **Excel Generation**: Apache POI 5.2.3
- **Email**: Spring Mail with Mailtrap (dev)

## Project Structure

```
vendorvault-clm/
├── src/
│   ├── main/
│   │   ├── java/com/orion/vendorvault/
│   │   │   ├── advice/              # Global controller advice for error handling
│   │   │   ├── controller/          # MVC Controllers (8 controllers)
│   │   │   ├── dto/                 # Data Transfer Objects
│   │   │   │   ├── request/         # Request DTOs (16 classes)
│   │   │   │   └── response/        # Response DTOs (13 classes)
│   │   │   ├── exception/           # Custom exceptions and handlers
│   │   │   ├── model/
│   │   │   │   ├── entity/          # JPA Entities (15 entities)
│   │   │   │   └── enums/           # Enums (16 types)
│   │   │   ├── repository/          # JPA Repositories (15 repositories)
│   │   │   ├── security/            # Security configuration
│   │   │   ├── service/             # Business logic layer
│   │   │   │   └── impl/            # Service implementations
│   │   │   ├── util/                # Utility classes (7 utilities)
│   │   │   ├── DataInitializer.java # Database seeding
│   │   │   └── VendorVaultApplication.java
│   │   └── resources/
│   │       ├── db/migration/        # Flyway SQL scripts (17 migrations)
│   │       ├── static/              # CSS, JS, images, fonts
│   │       ├── templates/           # Thymeleaf templates (30+ views)
│   │       └── application*.properties/yml
│   └── test/
│       ├── java/                    # Unit tests (to be added)
│       └── resources/               # Test resources
├── tools/                           # Utility JARs
├── .env.example                     # Environment variables template
├── .gitignore                       # Git ignore rules
├── docker-compose.yml               # Docker setup for MySQL & Mail
├── mvnw.cmd                         # Maven wrapper for Windows
└── pom.xml                          # Maven configuration
```


## Database Schema

### Core Tables

**1. roles**
- `role_id` (BIGINT, PK) - Primary key
- `role_name` (VARCHAR) - Role name (e.g., ROLE_SYSTEM_ADMIN)

**2. users**
- `user_id` (BIGINT, PK) - Primary key
- `employee_id` (VARCHAR, UNIQUE) - Employee identifier
- `full_name` (VARCHAR) - Full name
- `email` (VARCHAR, UNIQUE) - Email address
- `password_hash` (VARCHAR) - Encrypted password
- `designation` (VARCHAR) - Job title
- `active` (BOOLEAN) - Account status
- `account_locked` (BOOLEAN) - Lock status
- `failed_login_attempts` (INT) - Failed login counter
- `last_login_at` (TIMESTAMP) - Last login time
- `created_at`, `updated_at` (TIMESTAMP) - Audit fields

**3. user_roles** (Join Table)
- `user_id` (BIGINT, FK) - References users
- `role_id` (BIGINT, FK) - References roles

**4. vendors**
- `vendor_id` (BIGINT, PK) - Primary key
- `vendor_code` (VARCHAR, UNIQUE) - Vendor code (VND-XXXX)
- `name` (VARCHAR) - Vendor name
- `category` (VARCHAR) - Business category
- `pan` (VARCHAR) - PAN number
- `gstin` (VARCHAR) - GST number
- `msme_category` (ENUM) - MSME classification
- `status` (ENUM) - ACTIVE, BLACKLISTED, etc.
- `address`, `city`, `state`, `pincode` - Location details
- `contact_person`, `contact_email`, `contact_phone` - Contact info
- `blacklist_reason`, `blacklisted_at` - Blacklist details
- `created_at`, `updated_at` - Audit fields

**5. vendor_documents**
- `document_id` (BIGINT, PK) - Primary key
- `vendor_id` (BIGINT, FK) - References vendors
- `document_type` (ENUM) - PAN_CARD, GST_CERTIFICATE, etc.
- `file_name`, `file_path`, `file_size` - File details
- `uploaded_at` - Upload timestamp

**6. vendor_bank_details**
- `bank_detail_id` (BIGINT, PK) - Primary key
- `vendor_id` (BIGINT, FK) - References vendors
- `bank_name`, `branch_name` - Bank information
- `account_number`, `ifsc_code` - Account details
- `account_holder_name` - Account holder
- `is_primary` (BOOLEAN) - Primary account flag

**7. contracts**
- `contract_id` (BIGINT, PK) - Primary key
- `contract_number` (VARCHAR, UNIQUE) - Contract number (CTR-XXXXXXX)
- `title` (VARCHAR) - Contract title
- `vendor_id` (BIGINT, FK) - References vendors
- `contract_type` (ENUM) - SERVICE, PURCHASE, etc.
- `status` (ENUM) - DRAFT, ACTIVE, EXPIRED, etc.
- `start_date`, `end_date` (DATE) - Contract period
- `contract_value` (DECIMAL) - Contract value
- `currency` (VARCHAR) - Currency code
- `department` (VARCHAR) - Owning department
- `payment_terms` (TEXT) - Payment terms
- `created_by_user_id` (BIGINT, FK) - Creator
- `created_at`, `updated_at` - Audit fields

**8. contract_documents**
- `document_id` (BIGINT, PK) - Primary key
- `contract_id` (BIGINT, FK) - References contracts
- `document_type` (ENUM) - MAIN_CONTRACT, AMENDMENT, etc.
- `version` (INT) - Document version
- `file_name`, `file_path`, `file_size` - File details
- `uploaded_by_user_id` (BIGINT, FK) - Uploader
- `uploaded_at` - Upload timestamp

**9. contract_amendments**
- `amendment_id` (BIGINT, PK) - Primary key
- `contract_id` (BIGINT, FK) - References contracts
- `amendment_number` (VARCHAR) - Amendment identifier
- `reason` (TEXT) - Amendment reason
- `effective_date` (DATE) - Effective date
- `created_by_user_id` (BIGINT, FK) - Creator
- `created_at` - Creation timestamp

**10. approval_workflows**
- `workflow_id` (BIGINT, PK) - Primary key
- `contract_id` (BIGINT, FK) - References contracts
- `workflow_type` (ENUM) - CONTRACT_APPROVAL, AMENDMENT_APPROVAL, etc.
- `status` (ENUM) - PENDING, IN_PROGRESS, APPROVED, REJECTED
- `total_steps` (INT) - Total approval steps
- `current_step` (INT) - Current step number
- `initiated_by_user_id` (BIGINT, FK) - Initiator
- `initiated_at`, `completed_at` - Timestamps

**11. approval_steps**
- `step_id` (BIGINT, PK) - Primary key
- `workflow_id` (BIGINT, FK) - References approval_workflows
- `step_order` (INT) - Step sequence
- `role_required` (VARCHAR) - Required role
- `assigned_to_user_id` (BIGINT, FK) - Assigned user
- `action` (ENUM) - PENDING, APPROVED, REJECTED, DELEGATED
- `comments` (TEXT) - Approver comments
- `action_at`, `due_at` - Timestamps

**12. sla_monitors**
- `sla_id` (BIGINT, PK) - Primary key
- `contract_id` (BIGINT, FK) - References contracts
- `sla_type` (ENUM) - DELIVERY, RESPONSE_TIME, etc.
- `description` (TEXT) - SLA description
- `target_value` (VARCHAR) - Target metric
- `actual_value` (VARCHAR) - Actual metric
- `status` (ENUM) - MET, BREACHED, AT_RISK
- `breach_severity` (ENUM) - LOW, MEDIUM, HIGH, CRITICAL
- `measured_at` - Measurement timestamp

**13. performance_evaluations**
- `evaluation_id` (BIGINT, PK) - Primary key
- `vendor_id` (BIGINT, FK) - References vendors
- `evaluation_period` (VARCHAR) - Period (e.g., Q1 2024)
- `quality_score`, `delivery_score`, `communication_score`, `compliance_score` (INT) - Scores
- `overall_score` (DECIMAL) - Overall score
- `status` (ENUM) - DRAFT, FINALIZED
- `evaluator_user_id` (BIGINT, FK) - Evaluator
- `finalized_at` - Finalization timestamp

**14. notifications**
- `notification_id` (BIGINT, PK) - Primary key
- `user_id` (BIGINT, FK) - References users
- `type` (ENUM) - CONTRACT_EXPIRY, APPROVAL_REQUIRED, etc.
- `title`, `message` (TEXT) - Notification content
- `is_read` (BOOLEAN) - Read status
- `created_at` - Creation timestamp

**15. audit_logs**
- `log_id` (BIGINT, PK) - Primary key
- `user_id` (BIGINT, FK) - References users
- `event_type` (VARCHAR) - Event type
- `entity_type`, `entity_id` (VARCHAR) - Affected entity
- `description` (TEXT) - Event description
- `ip_address` (VARCHAR) - User IP
- `created_at` - Event timestamp

**16. persistent_logins** (Remember Me)
- `series` (VARCHAR, PK) - Token series
- `username` (VARCHAR) - Username
- `token` (VARCHAR) - Token value
- `last_used` (TIMESTAMP) - Last usage time

### Relationships

- Users ↔ Roles (Many-to-Many via user_roles)
- Vendors → Vendor Documents (One-to-Many)
- Vendors → Vendor Bank Details (One-to-Many)
- Vendors → Contracts (One-to-Many)
- Contracts → Contract Documents (One-to-Many)
- Contracts → Contract Amendments (One-to-Many)
- Contracts → Approval Workflows (One-to-Many)
- Approval Workflows → Approval Steps (One-to-Many)
- Contracts → SLA Monitors (One-to-Many)
- Vendors → Performance Evaluations (One-to-Many)
- Users → Notifications (One-to-Many)
- Users → Audit Logs (One-to-Many)


## Prerequisites

- **Java Development Kit (JDK)**: Version 17 or higher
- **Maven**: Version 3.9+ (or use included Maven wrapper)
- **MySQL**: Version 8.0 or higher
- **Docker** (Optional): For running MySQL and mail server via docker-compose
- **Git**: For version control

## Environment Setup

### 1. Clone the Repository

```bash
git clone <repository-url>
cd vendorvault-clm
```

### 2. Configure Environment Variables

Copy the example environment file and configure it:

```bash
copy .env.example .env
```

Edit `.env` file with your actual values:

```properties
# Database Configuration
DB_HOST=localhost
DB_PORT=3306
DB_NAME=vendorvault_db
DB_USERNAME=vendorvault_user
DB_PASSWORD=your_secure_password

# Mail Configuration
MAIL_HOST=smtp.mailtrap.io
MAIL_PORT=2525
MAIL_USERNAME=your_mailtrap_username
MAIL_PASSWORD=your_mailtrap_password

# Security
VENDORVAULT_REMEMBER_ME_KEY=generate_a_random_secure_key_here

# Application
SPRING_PROFILES_ACTIVE=dev
SERVER_PORT=8080

# File Storage
FILE_STORAGE_PATH=./uploads
```

### 3. Database Setup

#### Option A: Using Docker (Recommended for Development)

```bash
docker-compose up -d
```

This will start:
- MySQL 8.0 on port 3306
- Fake SMTP server on ports 1025 (SMTP) and 1080 (Web UI)

#### Option B: Manual MySQL Setup

1. Install MySQL 8.0
2. Create database and user:

```sql
CREATE DATABASE vendorvault_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'vendorvault_user'@'localhost' IDENTIFIED BY 'your_secure_password';
GRANT ALL PRIVILEGES ON vendorvault_db.* TO 'vendorvault_user'@'localhost';
FLUSH PRIVILEGES;
```

### 4. Build the Application

```bash
# Using Maven wrapper (Windows)
mvnw.cmd clean install

# Or using system Maven
mvn clean install
```

### 5. Run Database Migrations

Flyway migrations run automatically on application startup. The application will:
- Create all required tables
- Set up indexes and constraints
- Seed initial data (roles, admin user, sample data)


## Running the Application

### Development Mode

```bash
# Using Maven wrapper
mvnw.cmd spring-boot:run

# Or using system Maven
mvn spring-boot:run

# With specific profile
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

The application will start on `http://localhost:8080`

### Production Mode

```bash
# Build the JAR
mvn clean package -DskipTests

# Run the JAR
java -jar target/vendorvault-clm-1.0.0.jar --spring.profiles.active=prod
```

### Default Credentials

After first run, use these credentials to login:

- **Email**: admin@orion.com
- **Password**: admin123

**⚠️ IMPORTANT**: Change the default password immediately after first login!

## Application Features

### 1. User Management
- Role-based access control (6 roles)
- User creation and management
- Account locking after failed login attempts
- Password change functionality
- Session management

### 2. Vendor Management
- Vendor registration and onboarding
- Document upload (PAN, GST, MSME certificates)
- Bank details management
- Vendor status tracking (Active, Blacklisted, Under Review)
- MSME category classification
- Vendor search and filtering

### 3. Contract Management
- Contract creation and lifecycle tracking
- Multiple contract types (Service, Purchase, Lease, etc.)
- Contract status workflow (Draft → Pending Approval → Active → Expired)
- Contract amendments tracking
- Document version control
- Contract renewal management
- Contract termination with reasons
- Expiry alerts (30 days before expiration)

### 4. Approval Workflows
- Multi-step approval process
- Role-based approval routing
- Approval delegation
- Comments and rejection reasons
- Approval queue management
- Email notifications for pending approvals

### 5. SLA Monitoring
- SLA definition and tracking
- Breach detection and alerts
- Severity classification (Low, Medium, High, Critical)
- Performance metrics tracking

### 6. Performance Evaluation
- Vendor performance scoring
- Multiple evaluation criteria (Quality, Delivery, Communication, Compliance)
- Quarterly/Annual evaluation periods
- Performance history tracking

### 7. Notifications
- Real-time notifications
- Email notifications
- Contract expiry alerts
- Approval reminders
- System notifications

### 8. Reporting & Analytics
- Dashboard with KPIs
- Contract value analytics
- Vendor performance reports
- Expiring contracts report
- Approval pending reports
- PDF and Excel export capabilities

### 9. Audit Logging
- Complete audit trail
- User activity tracking
- Entity change tracking
- IP address logging
- Timestamp tracking


## API Endpoints

### Authentication
- `GET /login` - Login page
- `POST /authenticate` - Process login
- `GET /logout` - Logout

### Dashboard
- `GET /dashboard` - Main dashboard with KPIs

### User Management
- `GET /users` - List all users
- `GET /users/new` - Create user form
- `POST /users` - Save new user
- `GET /users/{id}/edit` - Edit user form
- `POST /users/{id}` - Update user
- `POST /users/{id}/lock` - Lock user account
- `POST /users/{id}/unlock` - Unlock user account

### Vendor Management
- `GET /vendors` - List all vendors
- `GET /vendors/new` - Create vendor form
- `POST /vendors` - Save new vendor
- `GET /vendors/{id}` - View vendor details
- `GET /vendors/{id}/edit` - Edit vendor form
- `POST /vendors/{id}` - Update vendor
- `POST /vendors/{id}/blacklist` - Blacklist vendor
- `POST /vendors/{id}/documents` - Upload vendor document
- `GET /vendors/search` - Search vendors

### Contract Management
- `GET /contracts` - List all contracts
- `GET /contracts/new` - Create contract form
- `POST /contracts` - Save new contract
- `GET /contracts/{id}` - View contract details
- `GET /contracts/{id}/edit` - Edit contract form
- `POST /contracts/{id}` - Update contract
- `POST /contracts/{id}/submit-approval` - Submit for approval
- `POST /contracts/{id}/renew` - Renew contract
- `POST /contracts/{id}/terminate` - Terminate contract
- `POST /contracts/{id}/documents` - Upload contract document
- `POST /contracts/{id}/amendments` - Create amendment

### Approval Management
- `GET /approvals/queue` - Approval queue
- `POST /approvals/{stepId}/approve` - Approve step
- `POST /approvals/{stepId}/reject` - Reject step
- `POST /approvals/{stepId}/delegate` - Delegate approval

### Notifications
- `GET /notifications` - List notifications
- `POST /notifications/{id}/read` - Mark as read
- `POST /notifications/read-all` - Mark all as read

### Reports
- `GET /reports` - Reports dashboard
- `GET /reports/contracts/expiring` - Expiring contracts report
- `GET /reports/vendors/performance` - Vendor performance report
- `GET /reports/export/pdf` - Export report as PDF
- `GET /reports/export/excel` - Export report as Excel

## User Roles & Permissions

### 1. ROLE_SYSTEM_ADMIN
- Full system access
- User management
- System configuration
- All vendor and contract operations
- Access to all reports

### 2. ROLE_PROCUREMENT_MANAGER
- Vendor management
- Contract creation and management
- Approval initiation
- Performance evaluation

### 3. ROLE_FINANCE_CONTROLLER
- Contract approval (financial aspects)
- Payment terms review
- Budget compliance verification
- Financial reports access

### 4. ROLE_LEGAL_COUNSEL
- Contract approval (legal aspects)
- Legal compliance review
- Contract terms verification
- Legal risk assessment

### 5. ROLE_EXECUTIVE_DIRECTOR
- Final approval authority
- Strategic decisions
- High-value contract approval
- Executive reports access

### 6. ROLE_DEPARTMENT_HEAD
- Department-specific contract management
- Budget approval for department
- Vendor selection for department
- Department performance reports


## Deployment

### Deployment to Production

#### 1. Prepare Production Environment

Set environment variables on your production server:

```bash
export DB_URL=jdbc:mysql://production-db-host:3306/vendorvault_db
export DB_USERNAME=prod_user
export DB_PASSWORD=secure_production_password
export MAIL_HOST=smtp.production-mail.com
export MAIL_PORT=587
export MAIL_USERNAME=noreply@orion.com
export MAIL_PASSWORD=mail_password
export VENDORVAULT_REMEMBER_ME_KEY=production_secure_random_key
export SPRING_PROFILES_ACTIVE=prod
```

#### 2. Build Production JAR

```bash
mvn clean package -DskipTests -Pprod
```

#### 3. Deploy Options

**Option A: Traditional Server Deployment**

```bash
# Copy JAR to server
scp target/vendorvault-clm-1.0.0.jar user@server:/opt/vendorvault/

# SSH to server and run
ssh user@server
cd /opt/vendorvault
java -jar vendorvault-clm-1.0.0.jar
```

**Option B: Docker Deployment**

Create `Dockerfile`:

```dockerfile
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY target/vendorvault-clm-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

Build and run:

```bash
docker build -t vendorvault-clm:1.0.0 .
docker run -d -p 8080:8080 --env-file .env vendorvault-clm:1.0.0
```

**Option C: Cloud Platform Deployment**

**Heroku:**
```bash
heroku create vendorvault-clm
heroku addons:create cleardb:ignite
heroku config:set SPRING_PROFILES_ACTIVE=prod
git push heroku main
```

**AWS Elastic Beanstalk:**
```bash
eb init -p java-17 vendorvault-clm
eb create vendorvault-prod
eb deploy
```

**Railway:**
```bash
railway login
railway init
railway up
```

#### 4. Production Checklist

- [ ] Set all environment variables
- [ ] Configure production database
- [ ] Set up SSL/TLS certificates
- [ ] Configure production mail server
- [ ] Set strong passwords for all accounts
- [ ] Enable firewall rules
- [ ] Set up backup strategy
- [ ] Configure monitoring and logging
- [ ] Test all critical features
- [ ] Set up CI/CD pipeline (optional)

### Reverse Proxy Configuration (Nginx)

```nginx
server {
    listen 80;
    server_name vendorvault.orion.com;

    location / {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```


## Maintenance & Operations

### Updating Dependencies

```bash
# Check for outdated dependencies
mvn versions:display-dependency-updates

# Update to latest versions
mvn versions:use-latest-releases

# Rebuild and test
mvn clean install
```

### Database Backup

```bash
# Backup MySQL database
mysqldump -u vendorvault_user -p vendorvault_db > backup_$(date +%Y%m%d).sql

# Restore from backup
mysql -u vendorvault_user -p vendorvault_db < backup_20240331.sql
```

### Log Management

Logs are stored in:
- Application logs: `logs/application.log`
- Spring logs: Console output (configure in `application.properties`)

Configure log rotation in `application.properties`:

```properties
logging.file.name=logs/application.log
logging.file.max-size=10MB
logging.file.max-history=30
```

### Adding New Features

1. **Create Entity** (if needed):
   - Add entity class in `model/entity/`
   - Create repository in `repository/`
   - Add Flyway migration in `db/migration/`

2. **Create Service**:
   - Define interface in `service/`
   - Implement in `service/impl/`

3. **Create Controller**:
   - Add controller in `controller/`
   - Define request/response DTOs in `dto/`

4. **Create Views**:
   - Add Thymeleaf templates in `templates/`
   - Add CSS/JS in `static/`

5. **Test**:
   - Write unit tests in `src/test/java/`
   - Run tests: `mvn test`

### Common Issues & Solutions

**Issue 1: Application fails to start - "Cannot create connection to database"**
- Solution: Check database is running, verify credentials in `.env` file

**Issue 2: Flyway migration fails**
- Solution: Check migration scripts, use `spring.flyway.clean-on-validation-error=true` in dev

**Issue 3: Login fails with correct credentials**
- Solution: Check password encoding, verify user is active and not locked

**Issue 4: File upload fails**
- Solution: Check `FILE_STORAGE_PATH` exists and has write permissions

**Issue 5: Emails not sending**
- Solution: Verify mail server configuration, check SMTP credentials

### Performance Optimization

1. **Enable Caching**:
   - Already configured with Caffeine
   - Adjust cache settings in `application.properties`

2. **Database Indexing**:
   - Indexes already created in `V17__create_indexes.sql`
   - Monitor slow queries and add indexes as needed

3. **Connection Pooling**:
   - Configure HikariCP settings:
   ```properties
   spring.datasource.hikari.maximum-pool-size=10
   spring.datasource.hikari.minimum-idle=5
   ```

4. **JVM Tuning**:
   ```bash
   java -Xms512m -Xmx2048m -jar vendorvault-clm-1.0.0.jar
   ```


## Architecture Overview

### Design Pattern: Layered Architecture

```
┌─────────────────────────────────────────┐
│         Presentation Layer              │
│  (Controllers + Thymeleaf Templates)    │
└─────────────────┬───────────────────────┘
                  │
┌─────────────────▼───────────────────────┐
│         Service Layer                   │
│  (Business Logic + Validation)          │
└─────────────────┬───────────────────────┘
                  │
┌─────────────────▼───────────────────────┐
│         Repository Layer                │
│  (Data Access + JPA Repositories)       │
└─────────────────┬───────────────────────┘
                  │
┌─────────────────▼───────────────────────┐
│         Database Layer                  │
│  (MySQL 8.0 + Flyway Migrations)        │
└─────────────────────────────────────────┘
```

### Data Flow

1. **User Request** → Controller receives HTTP request
2. **Validation** → Request DTO validated using Bean Validation
3. **Service Layer** → Business logic executed
4. **Repository** → Data persisted/retrieved via JPA
5. **Response** → Response DTO returned to view
6. **View Rendering** → Thymeleaf renders HTML response

### Security Flow

1. User submits login credentials
2. Spring Security intercepts request
3. CustomUserDetailsService loads user from database
4. Password verified using BCrypt
5. Authentication token created
6. Session established with JSESSIONID cookie
7. Subsequent requests authenticated via session

### Key Components

**Controllers**: Handle HTTP requests, delegate to services, return views
**Services**: Contain business logic, transaction management
**Repositories**: Data access layer using Spring Data JPA
**Entities**: JPA entities mapped to database tables
**DTOs**: Data transfer objects for API communication
**Utilities**: Helper classes for common operations

### State Management

- **Session-based**: User authentication via Spring Security sessions
- **Database-backed**: All application state persisted in MySQL
- **Caching**: Frequently accessed data cached with Caffeine

### Third-Party Integrations

- **iText 7**: PDF generation for reports and contracts
- **Apache POI**: Excel export functionality
- **Spring Mail**: Email notifications
- **Flyway**: Database version control and migrations


## Code Understanding

### Key Files Explained

**VendorVaultApplication.java**
- Main entry point for Spring Boot application
- Enables scheduling for background tasks (contract expiry checks, notifications)

**SecurityConfig.java**
- Configures Spring Security
- Defines URL access rules
- Sets up form-based authentication
- Configures session management
- BCrypt password encoding

**DataInitializer.java**
- Runs on application startup
- Seeds database with initial data:
  - 6 system roles
  - Default admin user
  - 45 test users
  - 15 sample vendors
  - 35 sample contracts
  - 5 performance evaluations

**ContractServiceImpl.java**
- Core business logic for contract management
- Handles contract lifecycle (create, update, approve, renew, terminate)
- Manages approval workflow initiation
- Sends notifications for contract events

**ApprovalServiceImpl.java**
- Manages multi-step approval workflows
- Processes approval/rejection actions
- Handles approval delegation
- Updates workflow status

**VendorServiceImpl.java**
- Vendor CRUD operations
- Document upload handling
- Vendor search and filtering
- Blacklist management

**UserServiceImpl.java**
- User management operations
- Password change functionality
- Account locking/unlocking
- Failed login attempt tracking

**ReportGeneratorUtil.java**
- PDF report generation using iText
- CSV export functionality
- Excel report generation using Apache POI

**FileStorageUtil.java**
- File upload handling
- File storage management
- File retrieval and deletion

**SecurityUtil.java**
- Security helper methods
- Current user retrieval
- Permission checking utilities

### Important Enums

**ContractStatus**: DRAFT, PENDING_APPROVAL, ACTIVE, EXPIRED, TERMINATED, RENEWED
**VendorStatus**: ACTIVE, INACTIVE, BLACKLISTED, UNDER_REVIEW
**WorkflowStatus**: PENDING, IN_PROGRESS, APPROVED, REJECTED, CANCELLED
**ApprovalAction**: PENDING, APPROVED, REJECTED, DELEGATED
**NotificationType**: CONTRACT_EXPIRY, APPROVAL_REQUIRED, APPROVAL_APPROVED, etc.

### Validation Rules

- Email must be unique and valid format
- Employee ID must be unique
- Vendor code auto-generated (VND-XXXX)
- Contract number auto-generated (CTR-XXXXXXX)
- PAN must be 10 characters
- GSTIN must be 15 characters
- Contract end date must be after start date
- File uploads limited to 10MB


## Testing

### Running Tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=UserServiceTest

# Run with coverage
mvn clean test jacoco:report
```

### Test Structure (To Be Implemented)

```
src/test/java/
├── com/orion/vendorvault/
│   ├── controller/          # Controller integration tests
│   ├── service/             # Service unit tests
│   ├── repository/          # Repository tests
│   └── security/            # Security tests
```

### Writing Tests

**Example Service Test:**

```java
@SpringBootTest
class ContractServiceTest {
    
    @Autowired
    private ContractService contractService;
    
    @Test
    void testCreateContract() {
        // Arrange
        ContractCreateRequestDto dto = new ContractCreateRequestDto();
        // ... set properties
        
        // Act
        ContractResponseDto result = contractService.createContract(dto);
        
        // Assert
        assertNotNull(result);
        assertEquals("Expected Title", result.getTitle());
    }
}
```

## Security Best Practices

### Implemented Security Measures

1. **Password Security**
   - BCrypt hashing with salt
   - Minimum password requirements (enforced at UI level)
   - Password change functionality

2. **Session Management**
   - Session timeout: 30 minutes
   - Single session per user (configurable)
   - Secure session cookies

3. **Authentication**
   - Form-based authentication
   - Account locking after 5 failed attempts
   - Remember-me functionality (optional)

4. **Authorization**
   - Role-based access control (RBAC)
   - Method-level security with @PreAuthorize
   - URL-based access restrictions

5. **Input Validation**
   - Bean Validation annotations
   - XSS prevention via Thymeleaf escaping
   - SQL injection prevention via JPA

6. **Audit Trail**
   - All user actions logged
   - IP address tracking
   - Timestamp recording

### Security Recommendations

- Change default admin password immediately
- Use strong passwords (min 12 characters, mixed case, numbers, symbols)
- Enable HTTPS in production
- Regularly update dependencies
- Implement rate limiting for login attempts
- Set up intrusion detection
- Regular security audits
- Backup encryption
- Database connection encryption


## Known Issues & Limitations

### Current Limitations

1. **No Multi-tenancy**: Single organization support only
2. **No REST API**: Currently MVC-only (REST API can be added)
3. **Limited File Types**: Document upload supports common formats only
4. **No Real-time Updates**: Requires page refresh for updates
5. **Single Language**: English only (i18n can be added)
6. **Basic Reporting**: Advanced analytics not implemented

### Planned Improvements

- [ ] Add comprehensive unit and integration tests
- [ ] Implement REST API for mobile/external integrations
- [ ] Add real-time notifications using WebSocket
- [ ] Implement advanced analytics dashboard
- [ ] Add multi-language support (i18n)
- [ ] Implement document e-signature integration
- [ ] Add contract template management
- [ ] Implement workflow designer UI
- [ ] Add bulk operations support
- [ ] Implement advanced search with Elasticsearch
- [ ] Add export to multiple formats (Word, CSV, JSON)
- [ ] Implement two-factor authentication (2FA)
- [ ] Add API rate limiting
- [ ] Implement GraphQL API
- [ ] Add mobile responsive improvements

## Contributing

### Development Workflow

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/your-feature`
3. Make changes and commit: `git commit -am 'Add new feature'`
4. Push to branch: `git push origin feature/your-feature`
5. Submit a pull request

### Code Style Guidelines

- Follow Java naming conventions
- Use Lombok annotations to reduce boilerplate
- Write meaningful commit messages
- Add JavaDoc for public methods
- Keep methods small and focused
- Use dependency injection
- Follow SOLID principles

### Commit Message Format

```
type(scope): subject

body

footer
```

Types: feat, fix, docs, style, refactor, test, chore

Example:
```
feat(contract): add contract renewal functionality

- Added renewal request DTO
- Implemented renewal service method
- Created renewal UI form

Closes #123
```

## License

This project is proprietary software developed for Orion Heavy Industries Ltd.
All rights reserved.

## Support & Contact

For support, please contact:
- **Email**: support@orion.com
- **Internal Helpdesk**: ext. 1234
- **Project Lead**: [Name]
- **Technical Lead**: [Name]

## Acknowledgments

- Spring Boot Team for the excellent framework
- Thymeleaf Team for the template engine
- All contributors to the open-source libraries used

---

**Version**: 1.0.0  
**Last Updated**: March 31, 2026  
**Developed by**: Orion Heavy Industries Ltd. IT Department
