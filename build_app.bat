@echo off
echo Stopping Gradle daemons...
call gradlew --stop

echo Killing Java processes...
taskkill /f /im java.exe >nul 2>&1

echo Waiting for processes to terminate...
timeout /t 3 /nobreak >nul

echo Attempting to clean build directory...
rmdir /s /q app\build >nul 2>&1

echo Building the application...
call gradlew app:assembleDebug --no-daemon --stacktrace

echo Build process completed.
pause
