@echo off
cd /d "%~dp0"
if not exist "bin" mkdir "bin"

echo ===========================================
echo  Compiling Hostel Management Dashboard...
echo ===========================================
javac -d "bin" HostelManagement.java Room.java Student.java HostelDashboard.java
if %ERRORLEVEL% NEQ 0 (
    echo.
    echo [ERROR] Compilation failed. Please check the errors above.
    pause
    exit /b %ERRORLEVEL%
)

echo.
echo [SUCCESS] Compilation successful!
echo Opening GUI Dashboard...
echo.
start "" javaw -cp "bin" HostelDashboard
if %ERRORLEVEL% NEQ 0 (
    java -cp "bin" HostelDashboard
)
