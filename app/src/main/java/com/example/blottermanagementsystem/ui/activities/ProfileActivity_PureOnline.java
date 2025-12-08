// PURE ONLINE REFACTORING - Replace these 3 methods in ProfileActivity:

// ========== 1. EDIT PROFILE - PURE ONLINE ==========
private void showEditProfileDialog() {
    if (currentUser == null) {
        android.widget.Toast.makeText(this, "User data not loaded", android.widget.Toast.LENGTH_SHORT).show();
        return;
    }
    
    // Inflate custom dialog layout
    android.view.View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_profile, null);
    com.google.android.material.textfield.TextInputEditText etFirstName = dialogView.findViewById(R.id.etFirstName);
    com.google.android.material.textfield.TextInputEditText etLastName = dialogView.findViewById(R.id.etLastName);
    com.google.android.material.button.MaterialButton btnCancelEdit = dialogView.findViewById(R.id.btnCancelEdit);
    com.google.android.material.button.MaterialButton btnSaveEdit = dialogView.findViewById(R.id.btnSaveEdit);
    
    // Pre-fill with current data
    etFirstName.setText(currentUser.getFirstName());
    etLastName.setText(currentUser.getLastName());
    
    // Create dialog
    androidx.appcompat.app.AlertDialog dialog = new androidx.appcompat.app.AlertDialog.Builder(this)
        .setView(dialogView)
        .setCancelable(false)
        .create();
    
    // Set button listeners
    btnCancelEdit.setOnClickListener(v -> dialog.dismiss());
    
    btnSaveEdit.setOnClickListener(v -> {
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        
        if (firstName.isEmpty() || lastName.isEmpty()) {
            android.widget.Toast.makeText(this, "Please fill all fields", android.widget.Toast.LENGTH_SHORT).show();
            return;
        }
        
        dialog.dismiss();
        updateProfileViaApi(firstName, lastName);
    });
    
    // Make dialog background transparent
    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    dialog.show();
}

/**
 * Pure Online: Update profile via API (Neon database only)
 */
private void updateProfileViaApi(String firstName, String lastName) {
    String userId = preferencesManager.getUserId();
    
    // ✅ PURE ONLINE: Check internet first
    com.example.blottermanagementsystem.utils.NetworkMonitor networkMonitor = 
        new com.example.blottermanagementsystem.utils.NetworkMonitor(this);
    
    if (!networkMonitor.isNetworkAvailable()) {
        android.util.Log.e("ProfileActivity", "❌ No internet connection");
        android.widget.Toast.makeText(this, "No internet connection", android.widget.Toast.LENGTH_SHORT).show();
        return;
    }
    
    android.util.Log.d("ProfileActivity", "🌐 Updating profile via API");
    
    com.example.blottermanagementsystem.utils.ApiClient.updateProfile(userId, firstName, lastName,
        new com.example.blottermanagementsystem.utils.ApiClient.ApiCallback<com.example.blottermanagementsystem.data.entity.User>() {
            @Override
            public void onSuccess(com.example.blottermanagementsystem.data.entity.User updatedUser) {
                android.util.Log.d("ProfileActivity", "✅ Profile updated successfully");
                currentUser = updatedUser;
                preferencesManager.setFirstName(firstName);
                preferencesManager.setLastName(lastName);
                
                runOnUiThread(() -> {
                    android.widget.Toast.makeText(ProfileActivity.this, "Profile updated successfully", android.widget.Toast.LENGTH_SHORT).show();
                    loadUserData();
                });
            }
            
            @Override
            public void onError(String errorMessage) {
                android.util.Log.e("ProfileActivity", "❌ Failed to update profile: " + errorMessage);
                runOnUiThread(() -> {
                    android.widget.Toast.makeText(ProfileActivity.this, "Failed to update profile: " + errorMessage, android.widget.Toast.LENGTH_SHORT).show();
                });
            }
        });
}

// ========== 2. CHANGE PASSWORD - PURE ONLINE ==========
private void showChangePasswordDialog() {
    // Inflate custom dialog layout
    android.view.View dialogView = getLayoutInflater().inflate(R.layout.dialog_change_password, null);
    com.google.android.material.textfield.TextInputEditText etCurrentPassword = dialogView.findViewById(R.id.etCurrentPassword);
    com.google.android.material.textfield.TextInputEditText etNewPassword = dialogView.findViewById(R.id.etNewPassword);
    com.google.android.material.textfield.TextInputEditText etConfirmPassword = dialogView.findViewById(R.id.etConfirmPassword);
    com.google.android.material.button.MaterialButton btnCancelPassword = dialogView.findViewById(R.id.btnCancelPassword);
    com.google.android.material.button.MaterialButton btnChangePassword = dialogView.findViewById(R.id.btnChangePassword);
    
    // Create dialog
    androidx.appcompat.app.AlertDialog dialog = new androidx.appcompat.app.AlertDialog.Builder(this)
        .setView(dialogView)
        .setCancelable(false)
        .create();
    
    // Set button listeners
    btnCancelPassword.setOnClickListener(v -> dialog.dismiss());
    
    btnChangePassword.setOnClickListener(v -> {
        String currentPassword = etCurrentPassword.getText().toString().trim();
        String newPassword = etNewPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();
        
        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            android.widget.Toast.makeText(this, "Please fill all fields", android.widget.Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (newPassword.length() < 6) {
            android.widget.Toast.makeText(this, "Password must be at least 6 characters", android.widget.Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (!newPassword.equals(confirmPassword)) {
            android.widget.Toast.makeText(this, "Passwords do not match", android.widget.Toast.LENGTH_SHORT).show();
            return;
        }
        
        dialog.dismiss();
        changePasswordViaApi(currentPassword, newPassword);
    });
    
    // Make dialog background transparent
    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    dialog.show();
}

/**
 * Pure Online: Change password via API (Neon database only)
 */
private void changePasswordViaApi(String currentPassword, String newPassword) {
    String userId = preferencesManager.getUserId();
    
    // ✅ PURE ONLINE: Check internet first
    com.example.blottermanagementsystem.utils.NetworkMonitor networkMonitor = 
        new com.example.blottermanagementsystem.utils.NetworkMonitor(this);
    
    if (!networkMonitor.isNetworkAvailable()) {
        android.util.Log.e("ProfileActivity", "❌ No internet connection");
        android.widget.Toast.makeText(this, "No internet connection", android.widget.Toast.LENGTH_SHORT).show();
        return;
    }
    
    android.util.Log.d("ProfileActivity", "🌐 Changing password via API");
    
    com.example.blottermanagementsystem.utils.ApiClient.changePassword(userId, currentPassword, newPassword,
        new com.example.blottermanagementsystem.utils.ApiClient.ApiCallback<Object>() {
            @Override
            public void onSuccess(Object response) {
                android.util.Log.d("ProfileActivity", "✅ Password changed successfully");
                runOnUiThread(() -> {
                    android.widget.Toast.makeText(ProfileActivity.this, "Password changed successfully", android.widget.Toast.LENGTH_SHORT).show();
                });
            }
            
            @Override
            public void onError(String errorMessage) {
                android.util.Log.e("ProfileActivity", "❌ Failed to change password: " + errorMessage);
                runOnUiThread(() -> {
                    android.widget.Toast.makeText(ProfileActivity.this, "Failed to change password: " + errorMessage, android.widget.Toast.LENGTH_SHORT).show();
                });
            }
        });
}

// ========== 3. DELETE ACCOUNT - PURE ONLINE ==========
private void performAccountDeletion() {
    String userId = preferencesManager.getUserId();
    
    // ✅ PURE ONLINE: Check internet first
    com.example.blottermanagementsystem.utils.NetworkMonitor networkMonitor = 
        new com.example.blottermanagementsystem.utils.NetworkMonitor(this);
    
    if (!networkMonitor.isNetworkAvailable()) {
        android.util.Log.e("ProfileActivity", "❌ No internet connection");
        android.widget.Toast.makeText(this, "No internet connection", android.widget.Toast.LENGTH_SHORT).show();
        return;
    }
    
    android.util.Log.d("ProfileActivity", "🌐 Deleting account via API");
    
    com.example.blottermanagementsystem.utils.ApiClient.deleteAccount(userId,
        new com.example.blottermanagementsystem.utils.ApiClient.ApiCallback<Object>() {
            @Override
            public void onSuccess(Object response) {
                android.util.Log.d("ProfileActivity", "✅ Account deleted successfully");
                
                runOnUiThread(() -> {
                    // Clear session
                    preferencesManager.clearSession();
                    
                    // Navigate to login
                    android.content.Intent intent = new android.content.Intent(ProfileActivity.this, LoginActivity.class);
                    intent.setFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK | android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                });
            }
            
            @Override
            public void onError(String errorMessage) {
                android.util.Log.e("ProfileActivity", "❌ Failed to delete account: " + errorMessage);
                runOnUiThread(() -> {
                    android.widget.Toast.makeText(ProfileActivity.this, "Failed to delete account: " + errorMessage, android.widget.Toast.LENGTH_SHORT).show();
                });
            }
        });
}
