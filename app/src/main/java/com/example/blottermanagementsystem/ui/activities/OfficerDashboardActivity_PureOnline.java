// PURE ONLINE REFACTORING - Replace loadData() method with this:

private void loadData() {
    com.example.blottermanagementsystem.utils.GlobalLoadingManager.show(this, "Loading dashboard...");

    // ✅ PURE ONLINE: Check internet first
    com.example.blottermanagementsystem.utils.NetworkMonitor networkMonitor = 
        new com.example.blottermanagementsystem.utils.NetworkMonitor(this);
    
    if (!networkMonitor.isNetworkAvailable()) {
        android.util.Log.e("OfficerDashboard", "❌ No internet connection");
        com.example.blottermanagementsystem.utils.GlobalLoadingManager.hide();
        Toast.makeText(this, "No internet connection. Please check your connection.", Toast.LENGTH_SHORT).show();
        return;
    }
    
    // Online - proceed with API call
    android.util.Log.d("OfficerDashboard", "🌐 Internet available - Loading cases from API");
    loadCasesViaApi();
}

/**
 * Pure Online: Load assigned cases via API (Neon database only)
 */
private void loadCasesViaApi() {
    com.example.blottermanagementsystem.utils.ApiClient.getAllReports(
        new com.example.blottermanagementsystem.utils.ApiClient.ApiCallback<List<BlotterReport>>() {
            @Override
            public void onSuccess(List<BlotterReport> allReports) {
                android.util.Log.d("OfficerDashboard", "✅ Reports loaded from API: " + allReports.size());
                
                // Get current officer ID
                String userId = preferencesManager.getUserId();
                
                // Filter reports assigned to current officer
                List<BlotterReport> assignedCases = new ArrayList<>();
                int total = 0, active = 0, resolved = 0, pending = 0;
                
                for (BlotterReport report : allReports) {
                    Integer assignedId = report.getAssignedOfficerId();
                    String status = report.getStatus() != null ? report.getStatus().toLowerCase() : "";
                    
                    // Check if officer is assigned
                    boolean isAssigned = false;
                    if (assignedId != null && assignedId.toString().equals(userId)) {
                        isAssigned = true;
                    }
                    
                    // Check multiple officers assignment
                    if (!isAssigned && report.getAssignedOfficerIds() != null && !report.getAssignedOfficerIds().isEmpty()) {
                        String[] officerIds = report.getAssignedOfficerIds().split(",");
                        for (String id : officerIds) {
                            if (id.trim().equals(userId)) {
                                isAssigned = true;
                                break;
                            }
                        }
                    }
                    
                    if (isAssigned) {
                        total++;
                        assignedCases.add(report);
                        
                        // Count by status
                        if ("assigned".equalsIgnoreCase(status) || "pending".equalsIgnoreCase(status)) {
                            pending++;
                        } else if ("ongoing".equalsIgnoreCase(status) || "in progress".equalsIgnoreCase(status) || "investigation".equalsIgnoreCase(status)) {
                            active++;
                        } else if ("resolved".equalsIgnoreCase(status) || "closed".equalsIgnoreCase(status) || "settled".equalsIgnoreCase(status)) {
                            resolved++;
                        }
                    }
                }
                
                // Sort by priority
                java.util.Collections.sort(assignedCases, (r1, r2) -> {
                    int p1 = getStatusPriority(r1.getStatus() != null ? r1.getStatus().toLowerCase() : "");
                    int p2 = getStatusPriority(r2.getStatus() != null ? r2.getStatus().toLowerCase() : "");
                    if (p1 != p2) return Integer.compare(p1, p2);
                    return Long.compare(r2.getDateFiled(), r1.getDateFiled());
                });
                
                final int finalTotal = total;
                final int finalActive = active;
                final int finalResolved = resolved;
                final int finalPending = pending;
                
                runOnUiThread(() -> {
                    recentCases.clear();
                    recentCases.addAll(assignedCases);
                    if (recentCaseAdapter != null) {
                        recentCaseAdapter.updateCases(recentCases);
                    }
                    
                    // Update counts
                    tvTotalCases.setText(String.valueOf(finalTotal));
                    tvActiveCases.setText(String.valueOf(finalActive));
                    tvResolvedCases.setText(String.valueOf(finalResolved));
                    tvPendingCases.setText(String.valueOf(finalPending));
                    
                    // Update UI visibility
                    if (emptyStateCard != null) emptyStateCard.setVisibility(View.VISIBLE);
                    if (assignedCases.isEmpty()) {
                        if (emptyState != null) emptyState.setVisibility(View.VISIBLE);
                        if (recyclerRecentCases != null) recyclerRecentCases.setVisibility(View.GONE);
                    } else {
                        if (emptyState != null) emptyState.setVisibility(View.GONE);
                        if (recyclerRecentCases != null) recyclerRecentCases.setVisibility(View.VISIBLE);
                    }
                    
                    com.example.blottermanagementsystem.utils.GlobalLoadingManager.hide();
                });
            }
            
            @Override
            public void onError(String errorMessage) {
                android.util.Log.e("OfficerDashboard", "❌ Failed to load cases: " + errorMessage);
                runOnUiThread(() -> {
                    com.example.blottermanagementsystem.utils.GlobalLoadingManager.hide();
                    Toast.makeText(OfficerDashboardActivity.this, "Failed to load cases: " + errorMessage, Toast.LENGTH_SHORT).show();
                });
            }
        });
}
