// PURE ONLINE REFACTORING - Add this method to NotificationsActivity:

/**
 * Pure Online: Load notifications via API (Neon database only)
 */
private void loadNotificationsViaApi(String userId) {
    com.example.blottermanagementsystem.utils.ApiClient.getNotifications(userId,
        new com.example.blottermanagementsystem.utils.ApiClient.ApiCallback<List<com.example.blottermanagementsystem.data.entity.Notification>>() {
            @Override
            public void onSuccess(List<com.example.blottermanagementsystem.data.entity.Notification> notifications) {
                android.util.Log.d("NotificationsActivity", "✅ Loaded " + notifications.size() + " notifications from API");
                
                // Check for unread
                boolean hasUnread = false;
                for (com.example.blottermanagementsystem.data.entity.Notification n : notifications) {
                    if (!n.isRead()) {
                        hasUnread = true;
                        break;
                    }
                }
                
                final boolean showMarkAllRead = hasUnread;
                final boolean isEmpty = notifications.isEmpty();
                
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    
                    if (isEmpty) {
                        if (recyclerView != null) recyclerView.setVisibility(View.GONE);
                        if (emptyState != null) emptyState.setVisibility(View.VISIBLE);
                        if (btnMarkAllRead != null) btnMarkAllRead.setVisibility(View.GONE);
                    } else {
                        if (recyclerView != null) recyclerView.setVisibility(View.VISIBLE);
                        if (emptyState != null) emptyState.setVisibility(View.GONE);
                        
                        try {
                            adapter = new NotificationAdapter(notifications,
                                NotificationsActivity.this::onNotificationClick,
                                NotificationsActivity.this::onNotificationLongClick);
                            adapter.setViewDetailsListener(NotificationsActivity.this::showNotificationDetails);
                            recyclerView.setAdapter(adapter);
                            android.util.Log.d("NotificationsActivity", "✅ Adapter set successfully");
                        } catch (Exception e) {
                            android.util.Log.e("NotificationsActivity", "❌ Error setting adapter: " + e.getMessage());
                        }
                        
                        if (btnMarkAllRead != null) {
                            btnMarkAllRead.setVisibility(showMarkAllRead ? View.VISIBLE : View.GONE);
                        }
                    }
                });
            }
            
            @Override
            public void onError(String errorMessage) {
                android.util.Log.e("NotificationsActivity", "❌ Failed to load notifications: " + errorMessage);
                runOnUiThread(() -> {
                    if (!isFinishing() && !isDestroyed()) {
                        Toast.makeText(NotificationsActivity.this, "Failed to load notifications", Toast.LENGTH_SHORT).show();
                        if (recyclerView != null) recyclerView.setVisibility(View.GONE);
                        if (emptyState != null) emptyState.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
}
