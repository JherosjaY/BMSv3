# Use Android SDK image as base
FROM android:33

# Set working directory
WORKDIR /app

# Copy project files
COPY . .

# Build the Android app
RUN ./gradlew build

# Expose port (if needed for testing)
EXPOSE 8080

# Default command
CMD ["echo", "Android APK built successfully"]
