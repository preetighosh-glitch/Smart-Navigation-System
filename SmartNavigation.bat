@echo off
set "DB_URL=jdbc:mysql://localhost:3306/smart_city_navigation"
set "DB_USER=root"
set /p "DB_PASSWORD=Enter MySQL password: "
cd /d "%~dp0"
dist\SmartNavigation\SmartNavigation.exe
pause
