# BMS v3 - API Endpoints Documentation

## Overview
This document lists all API endpoints required for each Activity in the Blotter Management System.

---

## 🔐 Authentication Endpoints

### 1. **LoginActivity**
**File:** `app/src/main/java/com/example/blottermanagementsystem/ui/activities/LoginActivity.java`

**Endpoints Used:**
- `POST /api/auth/login`
  - **Request:** `{ username: string, password: string }`
  - **Response:** `{ success: boolean, message: string, data: { user: {...}, token: string } }`
  - **Purpose:** User login

---

### 2. **RegisterActivity**
**File:** `app/src/main/java/com/example/blottermanagementsystem/ui/activities/RegisterActivity.java`

**Endpoints Used:**
- `POST /api/auth/register`
  - **Request:** `{ username: string, email: string, password: string, confirmPassword: string }`
  - **Response:** `{ success: boolean, message: string, data: { user: {...}, token: string } }`
  - **Purpose:** User registration (auto-verifies email, skips email verification)

---

### 3. **EmailVerificationActivity**
**File:** `app/src/main/java/com/example/blottermanagementsystem/ui/activities/EmailVerificationActivity.java`

**Endpoints Used:**
- `POST /api/auth/verify-email`
  - **Request:** `{ email: string, code: string }`
  - **Response:** `{ success: boolean, message: string }`
  - **Purpose:** Verify email with 6-digit code (currently disabled)

- `POST /api/auth/send-verification-code`
  - **Request:** `{ email: string }`
  - **Response:** `{ success: boolean, message: string }`
  - **Purpose:** Send verification code to email (currently disabled)

---

### 4. **ForgotPasswordActivity**
**File:** `app/src/main/java/com/example/blottermanagementsystem/ui/activities/ForgotPasswordActivity.java`

**Endpoints Used:**
- `POST /api/auth/forgot-password`
  - **Request:** `{ email: string }`
  - **Response:** `{ success: boolean, message: string }`
  - **Purpose:** Send password reset code to email

- `POST /api/auth/reset-password`
  - **Request:** `{ email: string, code: string, newPassword: string }`
  - **Response:** `{ success: boolean, message: string }`
  - **Purpose:** Reset password with code

---

### 5. **ChangePasswordActivity**
**File:** `app/src/main/java/com/example/blottermanagementsystem/ui/activities/ChangePasswordActivity.java`

**Endpoints Used:**
- `POST /api/auth/change-password`
  - **Request:** `{ userId: string, oldPassword: string, newPassword: string }`
  - **Response:** `{ success: boolean, message: string }`
  - **Purpose:** Change password for logged-in user

---

## 📊 Dashboard Endpoints

### 6. **AdminDashboardActivity**
**File:** `app/src/main/java/com/example/blottermanagementsystem/ui/activities/AdminDashboardActivity.java`

**Endpoints Used:**
- `GET /api/reports`
  - **Response:** `{ success: boolean, data: [{ id, caseNumber, incidentType, status, ... }] }`
  - **Purpose:** Get all blotter reports

- `GET /api/users`
  - **Response:** `{ success: boolean, data: [{ id, username, email, role, ... }] }`
  - **Purpose:** Get all users

- `GET /api/analytics/dashboard`
  - **Response:** `{ success: boolean, data: { totalReports, pendingReports, resolvedReports, ... } }`
  - **Purpose:** Get dashboard statistics

---

### 7. **OfficerDashboardActivity**
**File:** `app/src/main/java/com/example/blottermanagementsystem/ui/activities/OfficerDashboardActivity.java`

**Endpoints Used:**
- `GET /api/reports?assignedTo=userId`
  - **Response:** `{ success: boolean, data: [{ id, caseNumber, status, ... }] }`
  - **Purpose:** Get assigned reports for officer

- `GET /api/analytics/officer/:userId`
  - **Response:** `{ success: boolean, data: { assignedCases, resolvedCases, ... } }`
  - **Purpose:** Get officer statistics

---

## 📋 Blotter Report Endpoints

### 8. **AddReportActivity**
**File:** `app/src/main/java/com/example/blottermanagementsystem/ui/activities/AddReportActivity.java`

**Endpoints Used:**
- `POST /api/reports`
  - **Request:** `{ caseNumber, incidentType, incidentDate, incidentTime, incidentLocation, narrative, complainantName, complainantContact, complainantAddress, complainantEmail, status, priority, filedBy, audioRecordingUri, ... }`
  - **Response:** `{ success: boolean, message: string, data: { id, caseNumber, ... } }`
  - **Purpose:** Create new blotter report

- `POST /api/reports/:reportId/upload-audio`
  - **Request:** FormData with audio file
  - **Response:** `{ success: boolean, data: { audioUri } }`
  - **Purpose:** Upload audio recording

---

### 9. **EditReportActivity**
**File:** `app/src/main/java/com/example/blottermanagementsystem/ui/activities/EditReportActivity.java`

**Endpoints Used:**
- `GET /api/reports/:reportId`
  - **Response:** `{ success: boolean, data: { id, caseNumber, incidentType, ... } }`
  - **Purpose:** Get report details

- `PUT /api/reports/:reportId`
  - **Request:** `{ caseNumber, incidentType, status, priority, ... }`
  - **Response:** `{ success: boolean, message: string, data: { ... } }`
  - **Purpose:** Update report

- `DELETE /api/reports/:reportId`
  - **Response:** `{ success: boolean, message: string }`
  - **Purpose:** Delete report

---

### 10. **AdminCaseDetailActivity**
**File:** `app/src/main/java/com/example/blottermanagementsystem/ui/activities/AdminCaseDetailActivity.java`

**Endpoints Used:**
- `GET /api/reports/:reportId`
  - **Response:** `{ success: boolean, data: { ... } }`
  - **Purpose:** Get case details

- `PUT /api/reports/:reportId/assign`
  - **Request:** `{ assignedOfficerId: string }`
  - **Response:** `{ success: boolean, message: string }`
  - **Purpose:** Assign case to officer

- `PUT /api/reports/:reportId/status`
  - **Request:** `{ status: string }`
  - **Response:** `{ success: boolean, message: string }`
  - **Purpose:** Update case status

---

### 11. **OfficerCaseDetailActivity**
**File:** `app/src/main/java/com/example/blottermanagementsystem/ui/activities/OfficerCaseDetailActivity.java`

**Endpoints Used:**
- `GET /api/reports/:reportId`
  - **Response:** `{ success: boolean, data: { ... } }`
  - **Purpose:** Get case details

- `PUT /api/reports/:reportId/update-progress`
  - **Request:** `{ progress: number, notes: string }`
  - **Response:** `{ success: boolean, message: string }`
  - **Purpose:** Update case progress

---

## 👤 Profile Endpoints

### 12. **EditProfileActivity**
**File:** `app/src/main/java/com/example/blottermanagementsystem/ui/activities/EditProfileActivity.java`

**Endpoints Used:**
- `GET /api/users/:userId`
  - **Response:** `{ success: boolean, data: { id, username, email, firstName, lastName, phone, ... } }`
  - **Purpose:** Get user profile

- `PUT /api/users/:userId`
  - **Request:** `{ firstName, lastName, phone, department, ... }`
  - **Response:** `{ success: boolean, message: string, data: { ... } }`
  - **Purpose:** Update profile

- `PUT /api/auth/profile/:userId`
  - **Request:** `{ profilePhotoUri: string, profileCompleted: boolean }`
  - **Response:** `{ success: boolean, message: string }`
  - **Purpose:** Update profile photo and completion status

---

### 13. **ProfilePictureSelectionActivity**
**File:** `app/src/main/java/com/example/blottermanagementsystem/ui/activities/ProfilePictureSelectionActivity.java`

**Endpoints Used:**
- `POST /api/upload/profile-picture`
  - **Request:** FormData with image file
  - **Response:** `{ success: boolean, data: { imageUrl } }`
  - **Purpose:** Upload profile picture to Cloudinary

---

### 14. **AdminProfileActivity**
**File:** `app/src/main/java/com/example/blottermanagementsystem/ui/activities/AdminProfileActivity.java`

**Endpoints Used:**
- `GET /api/users/:userId`
  - **Response:** `{ success: boolean, data: { ... } }`
  - **Purpose:** Get admin profile

- `PUT /api/users/:userId`
  - **Request:** `{ firstName, lastName, ... }`
  - **Response:** `{ success: boolean, message: string }`
  - **Purpose:** Update admin profile

---

## 👮 Officer Management Endpoints

### 15. **AddOfficerActivity**
**File:** `app/src/main/java/com/example/blottermanagementsystem/ui/activities/AddOfficerActivity.java`

**Endpoints Used:**
- `POST /api/users`
  - **Request:** `{ username, email, password, firstName, lastName, role: "officer", department, badgeNumber, ... }`
  - **Response:** `{ success: boolean, message: string, data: { id, username, ... } }`
  - **Purpose:** Create new officer account

- `GET /api/departments`
  - **Response:** `{ success: boolean, data: [{ id, name, ... }] }`
  - **Purpose:** Get list of departments

---

### 16. **OfficerSettingsActivity**
**File:** `app/src/main/java/com/example/blottermanagementsystem/ui/activities/OfficerSettingsActivity.java`

**Endpoints Used:**
- `GET /api/users/:userId`
  - **Response:** `{ success: boolean, data: { ... } }`
  - **Purpose:** Get officer settings

- `PUT /api/users/:userId/settings`
  - **Request:** `{ notificationsEnabled, theme, language, ... }`
  - **Response:** `{ success: boolean, message: string }`
  - **Purpose:** Update officer settings

- `POST /api/fcm/register-token`
  - **Request:** `{ userId: string, fcmToken: string }`
  - **Response:** `{ success: boolean, message: string }`
  - **Purpose:** Register FCM token for push notifications

---

## 📞 Notification Endpoints

### 17. **NotificationsActivity**
**File:** `app/src/main/java/com/example/blottermanagementsystem/ui/activities/NotificationsActivity.java`

**Endpoints Used:**
- `GET /api/notifications/:userId`
  - **Response:** `{ success: boolean, data: [{ id, title, message, type, createdAt, ... }] }`
  - **Purpose:** Get user notifications

- `PUT /api/notifications/:notificationId/read`
  - **Response:** `{ success: boolean, message: string }`
  - **Purpose:** Mark notification as read

- `DELETE /api/notifications/:notificationId`
  - **Response:** `{ success: boolean, message: string }`
  - **Purpose:** Delete notification

---

## 📊 Analytics Endpoints

### 18. **OfficerAnalyticsActivity**
**File:** `app/src/main/java/com/example/blottermanagementsystem/ui/activities/OfficerAnalyticsActivity.java`

**Endpoints Used:**
- `GET /api/analytics/officer/:userId`
  - **Response:** `{ success: boolean, data: { assignedCases, resolvedCases, pendingCases, averageResolutionTime, ... } }`
  - **Purpose:** Get officer analytics

- `GET /api/analytics/officer/:userId/reports?startDate=...&endDate=...`
  - **Response:** `{ success: boolean, data: [{ date, count, ... }] }`
  - **Purpose:** Get reports by date range

---

## 📅 Hearing Endpoints

### 19. **HearingsActivity**
**File:** `app/src/main/java/com/example/blottermanagementsystem/ui/activities/HearingsActivity.java`

**Endpoints Used:**
- `GET /api/hearings`
  - **Response:** `{ success: boolean, data: [{ id, caseNumber, hearingDate, hearingTime, location, ... }] }`
  - **Purpose:** Get all hearings

- `POST /api/hearings`
  - **Request:** `{ reportId, hearingDate, hearingTime, location, notes, ... }`
  - **Response:** `{ success: boolean, message: string, data: { id, ... } }`
  - **Purpose:** Create new hearing

- `PUT /api/hearings/:hearingId`
  - **Request:** `{ hearingDate, hearingTime, location, notes, ... }`
  - **Response:** `{ success: boolean, message: string }`
  - **Purpose:** Update hearing

---

### 20. **HearingCalendarActivity**
**File:** `app/src/main/java/com/example/blottermanagementsystem/ui/activities/HearingCalendarActivity.java`

**Endpoints Used:**
- `GET /api/hearings?month=...&year=...`
  - **Response:** `{ success: boolean, data: [{ date, hearings: [...] }] }`
  - **Purpose:** Get hearings for specific month

---

## 📁 Evidence Endpoints

### 21. **EvidenceListActivity**
**File:** `app/src/main/java/com/example/blottermanagementsystem/ui/activities/EvidenceListActivity.java`

**Endpoints Used:**
- `GET /api/reports/:reportId/evidence`
  - **Response:** `{ success: boolean, data: [{ id, type, description, fileUrl, ... }] }`
  - **Purpose:** Get evidence for report

- `POST /api/reports/:reportId/evidence`
  - **Request:** FormData with evidence file
  - **Response:** `{ success: boolean, message: string, data: { id, fileUrl, ... } }`
  - **Purpose:** Upload evidence

- `DELETE /api/evidence/:evidenceId`
  - **Response:** `{ success: boolean, message: string }`
  - **Purpose:** Delete evidence

---

## 🔧 Admin Setup Endpoints

### 22. **AdminSetupActivity**
**File:** `app/src/main/java/com/example/blottermanagementsystem/ui/activities/AdminSetupActivity.java`

**Endpoints Used:**
- `GET /api/admin/config`
  - **Response:** `{ success: boolean, data: { systemName, version, ... } }`
  - **Purpose:** Get system configuration

- `PUT /api/admin/config`
  - **Request:** `{ systemName, theme, ... }`
  - **Response:** `{ success: boolean, message: string }`
  - **Purpose:** Update system configuration

- `POST /api/admin/backup`
  - **Response:** `{ success: boolean, message: string, data: { backupUrl } }`
  - **Purpose:** Create system backup

---

## 📊 Report Oversight Endpoints

### 23. **AdminReportOversightActivity**
**File:** `app/src/main/java/com/example/blottermanagementsystem/ui/activities/AdminReportOversightActivity.java`

**Endpoints Used:**
- `GET /api/reports`
  - **Response:** `{ success: boolean, data: [{ id, caseNumber, status, ... }] }`
  - **Purpose:** Get all reports

- `GET /api/reports?status=...&priority=...`
  - **Response:** `{ success: boolean, data: [...] }`
  - **Purpose:** Filter reports by status/priority

- `PUT /api/reports/:reportId/archive`
  - **Response:** `{ success: boolean, message: string }`
  - **Purpose:** Archive report

---

## 📱 Cases Endpoints

### 24. **MyAssignedCasesActivity**
**File:** `app/src/main/java/com/example/blottermanagementsystem/ui/activities/MyAssignedCasesActivity.java`

**Endpoints Used:**
- `GET /api/reports?assignedTo=userId`
  - **Response:** `{ success: boolean, data: [{ id, caseNumber, status, ... }] }`
  - **Purpose:** Get assigned cases

- `GET /api/reports?assignedTo=userId&status=...`
  - **Response:** `{ success: boolean, data: [...] }`
  - **Purpose:** Filter assigned cases by status

---

## ✅ Summary

**Total Endpoints Needed: ~40+**

### By Category:
- **Authentication:** 6 endpoints
- **Reports:** 8 endpoints
- **Users/Profile:** 8 endpoints
- **Notifications:** 3 endpoints
- **Analytics:** 2 endpoints
- **Hearings:** 3 endpoints
- **Evidence:** 3 endpoints
- **Admin:** 4 endpoints
- **FCM/Push:** 1 endpoint

---

## 🚀 Implementation Checklist

- [ ] All endpoints implemented in backend
- [ ] All endpoints tested with Postman/Insomnia
- [ ] Error handling added to all endpoints
- [ ] Authentication/Authorization verified
- [ ] Database migrations completed
- [ ] API documentation updated
- [ ] Frontend integration tested

---

**Last Updated:** 2025-12-07
**Status:** Documentation Complete ✅
