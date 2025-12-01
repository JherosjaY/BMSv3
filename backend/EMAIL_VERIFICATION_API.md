# Email Verification API Endpoints

## Overview
These endpoints handle email verification for user registration and password reset flows.

---

## Endpoints

### 1. POST `/api/email/send-verification-code`
Send a verification code to user's email.

**Request:**
```json
{
  "email": "user@example.com"
}
```

**Response (200):**
```json
{
  "success": true,
  "message": "Verification code sent to email",
  "data": {
    "email": "user@example.com",
    "expiresIn": 300
  }
}
```

**Error Response (500):**
```json
{
  "success": false,
  "message": "Failed to send verification code",
  "error": "Error details"
}
```

---

### 2. POST `/api/email/verify-email-code`
Verify the code entered by user.

**Request:**
```json
{
  "email": "user@example.com",
  "code": "123456"
}
```

**Response (200) - Valid Code:**
```json
{
  "success": true,
  "message": "Email verified successfully",
  "valid": true,
  "data": {
    "email": "user@example.com",
    "verified": true
  }
}
```

**Response (200) - Invalid Code:**
```json
{
  "success": false,
  "message": "Invalid verification code",
  "valid": false
}
```

**Response (200) - Expired Code:**
```json
{
  "success": false,
  "message": "Verification code has expired",
  "valid": false
}
```

---

### 3. POST `/api/email/resend-verification-code`
Resend verification code to email.

**Request:**
```json
{
  "email": "user@example.com"
}
```

**Response (200):**
```json
{
  "success": true,
  "message": "Verification code resent to email",
  "data": {
    "email": "user@example.com",
    "expiresIn": 300
  }
}
```

---

## Email Template

The verification code is sent with a professional HTML template including:
- App branding
- 6-digit code in large, centered format
- Expiration time (5 minutes)
- Security notice
- Footer with copyright

---

## Implementation Details

### Code Generation
- 6-digit random code
- Format: `100000-999999`
- Generated fresh for each request

### Code Storage
- **Current:** In-memory Map (development only)
- **Production:** Should use database with TTL
- **Expiration:** 5 minutes (300 seconds)

### Email Service
- **Provider:** Gmail (via Nodemailer)
- **From:** `process.env.GMAIL_USER`
- **Authentication:** `process.env.GMAIL_PASSWORD`

---

## Environment Variables Required

```env
GMAIL_USER=official.bms.2025@gmail.com
GMAIL_PASSWORD=your-app-password
```

---

## Android Integration

### Endpoints Called:
1. **Send Code:** `POST /api/email/send-verification-code`
2. **Verify Code:** `POST /api/email/verify-email-code`
3. **Resend Code:** `POST /api/email/resend-verification-code`

### Backend URL Configuration:
Update the following in `EmailVerificationActivity.java`:
```java
String backendUrl = "http://your-backend-url.com/api/email/...";
```

Replace `http://your-backend-url.com` with:
- **Development:** `http://localhost:3000`
- **Production:** `https://bms-1op6.onrender.com`

---

## Testing

### Test Code:
For development, you can manually set a code in the backend or use:
```
Email: test@example.com
Code: 123456 (if hardcoded for testing)
```

### Manual Testing:
1. Send verification code to email
2. Check email for code
3. Enter code in app
4. Verify success response

---

## Production Checklist

- [ ] Replace in-memory storage with database
- [ ] Implement proper error handling
- [ ] Add rate limiting for code requests
- [ ] Add logging for security audit
- [ ] Test email delivery
- [ ] Update backend URL in Android app
- [ ] Test with actual email addresses
- [ ] Monitor email sending failures
- [ ] Set up email retry mechanism
