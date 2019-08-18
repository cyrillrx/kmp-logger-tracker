# Description
Android logger and tracker components.

# Publish to Bintray

## Build scripts
```
   ./gradlew clean build bintrayUpload -p logger-crashlytics -PbintrayUser=USER -PbintrayKey=KEY -PdryRun=false
   ./gradlew clean build bintrayUpload -p logger-firebase -PbintrayUser=USER -PbintrayKey=KEY -PdryRun=false
   ./gradlew clean build bintrayUpload -p logger-logcat -PbintrayUser=USER -PbintrayKey=KEY -PdryRun=false
   
   ./gradlew clean build bintrayUpload -p notifier -PbintrayUser=USER -PbintrayKey=KEY -PdryRun=false
  
   ./gradlew clean build bintrayUpload -p tracker-amplitude -PbintrayUser=USER -PbintrayKey=KEY -PdryRun=false
   ./gradlew clean build bintrayUpload -p tracker-answers -PbintrayUser=USER -PbintrayKey=KEY -PdryRun=false
   ./gradlew clean build bintrayUpload -p tracker-firebase -PbintrayUser=USER -PbintrayKey=KEY -PdryRun=false
   ./gradlew clean build bintrayUpload -p tracker-google-analytics -PbintrayUser=USER -PbintrayKey=KEY -PdryRun=false
```