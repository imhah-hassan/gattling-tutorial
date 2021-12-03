SET BUNDLE=C:\Users\h.imhah\Desktop\webinaire\gatling-bundle

REM rmdir /S /Q "%BUNDLE%\user-files\resources"
REM rmdir /S /Q "%BUNDLE%\user-files\simulations\*"

REM mkdir "%BUNDLE%\user-files\simulations"
REM mkdir "%BUNDLE%\user-files\resources"

robocopy /E /S /XO "S:\github\gattling-tutorial\OrangeHRM\src\test\resources" %BUNDLE%\user-files\resources
robocopy /E /S /XO "S:\github\gattling-tutorial\OrangeHRM\src\test\scala" %BUNDLE%\user-files\simulations

pause
